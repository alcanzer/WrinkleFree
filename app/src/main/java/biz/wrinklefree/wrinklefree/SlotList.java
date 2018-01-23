package biz.wrinklefree.wrinklefree;

/**
 * Created by alcanzer on 1/20/18.
 */

public class SlotList {

    private String modifiedTimestamp;

    private String createdTimestamp;

    private String active;

    private String slotId;

    private String slotTiming;

    public String getModifiedTimestamp ()
    {
        return modifiedTimestamp;
    }

    public void setModifiedTimestamp (String modifiedTimestamp)
    {
        this.modifiedTimestamp = modifiedTimestamp;
    }

    public String getCreatedTimestamp ()
    {
        return createdTimestamp;
    }

    public void setCreatedTimestamp (String createdTimestamp)
    {
        this.createdTimestamp = createdTimestamp;
    }

    public String getActive ()
    {
        return active;
    }

    public void setActive (String active)
    {
        this.active = active;
    }

    public String getSlotId ()
    {
        return slotId;
    }

    public void setSlotId (String slotId)
    {
        this.slotId = slotId;
    }

    public String getSlotTiming ()
    {
        return slotTiming;
    }

    public void setSlotTiming (String slotTiming)
    {
        this.slotTiming = slotTiming;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [modifiedTimestamp = "+modifiedTimestamp+", createdTimestamp = "+createdTimestamp+", active = "+active+", slotId = "+slotId+", slotTiming = "+slotTiming+"]";
    }

}
