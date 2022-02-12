package com.rsvp.hml;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jooq.generated.tables.RsvpDocument.RSVP_DOCUMENT;
import jooq.generated.tables.pojos.RsvpDocument;
import org.jooq.DSLContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class HmlHandler {

    private static final Logger logger = Logger.getLogger(HmlHandler.class.getName());

    private static final long DELAY_MS = 100L;
    private static final long RETRIES = 2L;
    private static final long THRESHOLD = 1000L;

    private final RsvpsKafkaProducer rsvpsKafkaProducer;
    private final DummyBusinessLogic dummyBusinessLogic;
    private final DSLContext ctx;

    public HmlHandler(RsvpsKafkaProducer rsvpsKafkaProducer,
            DummyBusinessLogic dummyBusinessLogic, DSLContext ctx) {
        this.rsvpsKafkaProducer = rsvpsKafkaProducer;
        this.dummyBusinessLogic = dummyBusinessLogic;
        this.ctx = ctx;
    }

    public void recoverAfterRestart() {
        Flux.from(ctx.selectCount().from(RSVP_DOCUMENT))
                .subscribe(count -> {
                    if (count.into(int.class) > 0) {
                        logger.info("Recover after restart ...");

                        recoverRsvps();
                    }
                });
    }

    // RBML
    public void insertRsvps(String message) {
        Flux<RsvpDocument> fluxInsertRsvp = Flux.from(ctx.insertInto(RSVP_DOCUMENT)
                .columns(RSVP_DOCUMENT.ID, RSVP_DOCUMENT.RSVP, RSVP_DOCUMENT.STATUS)
                .values((long) Instant.now().getNano(), message, "PENDING")
                .returningResult(RSVP_DOCUMENT.ID, RSVP_DOCUMENT.RSVP, RSVP_DOCUMENT.STATUS))
                .map(rsvp -> new RsvpDocument(rsvp.value1(), rsvp.value2(), rsvp.value3()));

        processRsvp(fluxInsertRsvp);
    }

    // SBML
    private void recoverRsvps() {
        Flux<RsvpDocument> fluxFindAllRsvps = Flux.from(
                ctx.select(RSVP_DOCUMENT.ID, RSVP_DOCUMENT.RSVP, RSVP_DOCUMENT.STATUS)
                        .from(RSVP_DOCUMENT))
                .map(rsvp -> new RsvpDocument(rsvp.value1(), rsvp.value2(), rsvp.value3()));

        processRsvp(fluxFindAllRsvps);
    }

    private void removeRsvp(RsvpDocument rsvp) {

        Flux.from(ctx.deleteFrom(RSVP_DOCUMENT).where(RSVP_DOCUMENT.ID.eq(rsvp.getId())))
                .retry(RETRIES)
                .subscribe();
    }

    private void updateRsvp(RsvpDocument rsvp) {

        Flux.from(ctx.update(RSVP_DOCUMENT)
                .set(RSVP_DOCUMENT.STATUS, "FAILED")
                .where(RSVP_DOCUMENT.ID.eq(rsvp.getId())))
                .retry(RETRIES)
                .subscribe();
    }

    @Scheduled(
            initialDelayString = "${initialDelay.in.milliseconds}",
            fixedRateString = "${fixedRate.in.milliseconds}"
    )
    private void internalRecoverOfFailures() {

        logger.info("Scheduled job for failed RSVP ...");

        Flux.from(ctx.selectCount().from(RSVP_DOCUMENT)
                .where(RSVP_DOCUMENT.STATUS.eq("FAILED")))
                .subscribe(count -> {
                    logger.log(Level.INFO, "Failures found during scheduled job:\n{0}", count);

                    if (count.into(int.class) > THRESHOLD) {
                        logger.info("Failures threshold exceeded ...");

                        // Do one or all:
                        //  - stop ingesting RSVPs
                        //  - send a notification                        
                        //  - replay failures
                        //  - ...
                    } else if (count.into(int.class) > 0) {
                        logger.info("Replay failures ...");

                        recoverFailedRsvps();
                    }
                });
    }

    private void recoverFailedRsvps() {
        Flux<RsvpDocument> fluxFindAllFailedRsvps
                = Flux.from(ctx.select(RSVP_DOCUMENT.ID, RSVP_DOCUMENT.RSVP, RSVP_DOCUMENT.STATUS)
                        .from(RSVP_DOCUMENT)
                        .where(RSVP_DOCUMENT.STATUS.eq("FAILED")))
                        .map(rsvp -> new RsvpDocument(rsvp.value1(), rsvp.value2(), rsvp.value3()));

        processRsvp(fluxFindAllFailedRsvps);
    }

    // common helper for RBML and SBML
    private void processRsvp(Flux<RsvpDocument> rsvpDocument) {

        rsvpDocument.map(dummyBusinessLogic::dummyLogic)
                .delayElements(Duration.ofMillis(DELAY_MS))
                .doOnNext(rsvpsKafkaProducer::sendRsvpMessage)
                .subscribe(t -> {
                    if (t.getStatus().equals("PROCESSED")) {
                        logger.info(() -> "Deleting the RSVP with id: " + t.getId());
                        removeRsvp(t);

                    } else if (t.getStatus().equals("UNSENT")) {
                        logger.info(() -> "Mark as 'FAILED' the RSVP with id: " + t.getId());
                        t.setStatus("FAILED");
                        updateRsvp(t);
                    }
                });
    }
}
