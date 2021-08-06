package com.rsvp.hml;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import jooq.generated.tables.pojos.RsvpDocument;
import org.springframework.stereotype.Component;

@Component
// @EnableBinding(Source.class)
public class RsvpsKafkaProducer {

    private static final Logger logger
            = Logger.getLogger(RsvpsKafkaProducer.class.getName());

    private static final int SENDING_MESSAGE_TIMEOUT_MS = 1000;

    /*
    private final Source source;

    public RsvpsKafkaProducer(Source source) {
        this.source = source;
    }
    */

    public void sendRsvpMessage(RsvpDocument rsvp) {

        logger.info(() -> "Sending RSVP with id: " + rsvp.getId());

        try {
            /*
            source.output()
                    .send(MessageBuilder.withPayload(rsvp.getData())
                            .build(),
                            SENDING_MESSAGE_TIMEOUT_MS);
            */
            
            // remove the following line if you use the "source"
            if(new Random().nextBoolean()){
                throw new Exception("Triggered a dummy exception (like Kafka has failed receiving the RSVP) ...");
            }
            
            rsvp.setStatus("PROCESSED");
        } catch (Exception e) {
            if(rsvp.getStatus().equals("PENDING")) {
               rsvp.setStatus("UNSENT");
            }
            
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

}
