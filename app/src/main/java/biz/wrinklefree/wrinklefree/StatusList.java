package biz.wrinklefree.wrinklefree;

/**
 * Created by alcanzer on 1/20/18.
 */

public class StatusList {

    private String statusId;

    private String modifiedTimestamp;

    private String createdTimestamp;

    private String status;

    private String active;

    public String getStatusId ()
    {
        return statusId;
    }

    public void setStatusId (String statusId)
    {
        this.statusId = statusId;
    }

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

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getActive ()
    {
        return active;
    }

    public void setActive (String active)
    {
        this.active = active;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [statusId = "+statusId+", modifiedTimestamp = "+modifiedTimestamp+", createdTimestamp = "+createdTimestamp+", status = "+status+", active = "+active+"]";
    }

}
