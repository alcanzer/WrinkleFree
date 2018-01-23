package biz.wrinklefree.wrinklefree;

/**
 * Created by alcanzer on 1/20/18.
 */

public class ServiceTypeList {

    private String modifiedTimestamp;

    private String createdTimestamp;

    private String serviceTypeId;

    private String active;

    private String serviceName;

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

    public String getServiceTypeId ()
    {
        return serviceTypeId;
    }

    public void setServiceTypeId (String serviceTypeId)
    {
        this.serviceTypeId = serviceTypeId;
    }

    public String getActive ()
    {
        return active;
    }

    public void setActive (String active)
    {
        this.active = active;
    }

    public String getServiceName ()
    {
        return serviceName;
    }

    public void setServiceName (String serviceName)
    {
        this.serviceName = serviceName;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [modifiedTimestamp = "+modifiedTimestamp+", createdTimestamp = "+createdTimestamp+", serviceTypeId = "+serviceTypeId+", active = "+active+", serviceName = "+serviceName+"]";
    }

}
