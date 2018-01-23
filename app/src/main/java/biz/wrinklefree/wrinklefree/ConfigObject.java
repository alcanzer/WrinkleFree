package biz.wrinklefree.wrinklefree;

/**
 * Created by alcanzer on 1/20/18.
 */

public class ConfigObject {

    private SlotList[] slotList;

    private ServiceTypeList[] serviceTypeList;

    private StatusList[] statusList;

    public SlotList[] getSlotList ()
    {
        return slotList;
    }

    public void setSlotList (SlotList[] slotList)
    {
        this.slotList = slotList;
    }

    public ServiceTypeList[] getServiceTypeList ()
    {
        return serviceTypeList;
    }

    public void setServiceTypeList (ServiceTypeList[] serviceTypeList)
    {
        this.serviceTypeList = serviceTypeList;
    }

    public StatusList[] getStatusList ()
    {
        return statusList;
    }

    public void setStatusList (StatusList[] statusList)
    {
        this.statusList = statusList;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [slotList = "+slotList+", serviceTypeList = "+serviceTypeList+", statusList = "+statusList+"]";
    }

}
