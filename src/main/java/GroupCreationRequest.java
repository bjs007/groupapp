
public class GroupCreationRequest
{
    private String groupNameInEng;
    private String groupNameInHin;
    private String groupDescInEng;
    private String groupDescInHin;
    private String groupDetailInEng;
    private String groupDetailInHin;

    public GroupCreationRequest()
    {

    }

    public GroupCreationRequest(String groupNameInEng, String groupDescInEng,  String groupNameInHin, String groupDescInHin) {
        this.groupNameInEng = groupNameInEng;
        this.groupNameInHin = groupNameInHin;
        this.groupDescInEng = groupDescInEng;
        this.groupDescInHin = groupDescInHin;
    }

    public GroupCreationRequest(String groupNameInEng, String groupDescInEng, String groupDetailInEng,  String groupNameInHin, String groupDescInHin, String groupDetailInHin) {
        this.groupNameInEng = groupNameInEng;
        this.groupNameInHin = groupNameInHin;
        this.groupDescInEng = groupDescInEng;
        this.groupDescInHin = groupDescInHin;
        this.groupDetailInEng = groupDetailInEng;
        this.groupDetailInHin = groupDetailInHin;
    }

    public String getGroupNameInEng() {
        return groupNameInEng;
    }

    public void setGroupNameInEng(String groupNameInEng) {
        this.groupNameInEng = groupNameInEng;
    }

    public String getGroupNameInHin() {
        return groupNameInHin;
    }

    public void setGroupNameInHin(String groupNameInHin) {
        this.groupNameInHin = groupNameInHin;
    }

    public String getGroupDescInEng() {
        return groupDescInEng;
    }

    public void setGroupDescInEng(String groupDescInEng) {
        this.groupDescInEng = groupDescInEng;
    }

    public String getGroupDescInHin() {
        return groupDescInHin;
    }

    public void setGroupDescInHin(String groupDescInHin) {
        this.groupDescInHin = groupDescInHin;
    }

    public String getGroupDetailInEng() {
        return groupDetailInEng;
    }

    public void setGroupDetailInEng(String groupDetailInEng) {
        this.groupDetailInEng = groupDetailInEng;
    }

    public String getGroupDetailInHin() {
        return groupDetailInHin;
    }

    public void setGroupDetailInHin(String groupDetailInHin) {
        this.groupDetailInHin = groupDetailInHin;
    }
}
