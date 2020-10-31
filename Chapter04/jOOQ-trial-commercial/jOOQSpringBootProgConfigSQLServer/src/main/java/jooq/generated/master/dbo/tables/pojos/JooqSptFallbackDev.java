/*
 * This file is generated by jOOQ.
 */
package jooq.generated.master.dbo.tables.pojos;


import java.io.Serializable;
import java.time.LocalDateTime;

import javax.annotation.processing.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.13.5",
        "schema version:1.1"
    },
    date = "2020-10-31T06:19:36.439Z",
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class JooqSptFallbackDev implements Serializable {

    private static final long serialVersionUID = -464730690;

    private String        xserverName;
    private LocalDateTime xdttmIns;
    private LocalDateTime xdttmLastInsUpd;
    private Integer       xfallbackLow;
    private String        xfallbackDrive;
    private Integer       low;
    private Integer       high;
    private Short         status;
    private String        name;
    private String        phyname;

    public JooqSptFallbackDev() {}

    public JooqSptFallbackDev(JooqSptFallbackDev value) {
        this.xserverName = value.xserverName;
        this.xdttmIns = value.xdttmIns;
        this.xdttmLastInsUpd = value.xdttmLastInsUpd;
        this.xfallbackLow = value.xfallbackLow;
        this.xfallbackDrive = value.xfallbackDrive;
        this.low = value.low;
        this.high = value.high;
        this.status = value.status;
        this.name = value.name;
        this.phyname = value.phyname;
    }

    public JooqSptFallbackDev(
        String        xserverName,
        LocalDateTime xdttmIns,
        LocalDateTime xdttmLastInsUpd,
        Integer       xfallbackLow,
        String        xfallbackDrive,
        Integer       low,
        Integer       high,
        Short         status,
        String        name,
        String        phyname
    ) {
        this.xserverName = xserverName;
        this.xdttmIns = xdttmIns;
        this.xdttmLastInsUpd = xdttmLastInsUpd;
        this.xfallbackLow = xfallbackLow;
        this.xfallbackDrive = xfallbackDrive;
        this.low = low;
        this.high = high;
        this.status = status;
        this.name = name;
        this.phyname = phyname;
    }

    @NotNull
    @Size(max = 30)
    public String getXserverName() {
        return this.xserverName;
    }

    public void setXserverName(String xserverName) {
        this.xserverName = xserverName;
    }

    @NotNull
    public LocalDateTime getXdttmIns() {
        return this.xdttmIns;
    }

    public void setXdttmIns(LocalDateTime xdttmIns) {
        this.xdttmIns = xdttmIns;
    }

    @NotNull
    public LocalDateTime getXdttmLastInsUpd() {
        return this.xdttmLastInsUpd;
    }

    public void setXdttmLastInsUpd(LocalDateTime xdttmLastInsUpd) {
        this.xdttmLastInsUpd = xdttmLastInsUpd;
    }

    public Integer getXfallbackLow() {
        return this.xfallbackLow;
    }

    public void setXfallbackLow(Integer xfallbackLow) {
        this.xfallbackLow = xfallbackLow;
    }

    @Size(max = 2)
    public String getXfallbackDrive() {
        return this.xfallbackDrive;
    }

    public void setXfallbackDrive(String xfallbackDrive) {
        this.xfallbackDrive = xfallbackDrive;
    }

    @NotNull
    public Integer getLow() {
        return this.low;
    }

    public void setLow(Integer low) {
        this.low = low;
    }

    @NotNull
    public Integer getHigh() {
        return this.high;
    }

    public void setHigh(Integer high) {
        this.high = high;
    }

    @NotNull
    public Short getStatus() {
        return this.status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    @NotNull
    @Size(max = 30)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    @Size(max = 127)
    public String getPhyname() {
        return this.phyname;
    }

    public void setPhyname(String phyname) {
        this.phyname = phyname;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("JooqSptFallbackDev (");

        sb.append(xserverName);
        sb.append(", ").append(xdttmIns);
        sb.append(", ").append(xdttmLastInsUpd);
        sb.append(", ").append(xfallbackLow);
        sb.append(", ").append(xfallbackDrive);
        sb.append(", ").append(low);
        sb.append(", ").append(high);
        sb.append(", ").append(status);
        sb.append(", ").append(name);
        sb.append(", ").append(phyname);

        sb.append(")");
        return sb.toString();
    }
}
