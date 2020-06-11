
public class ChildNodeWithDBReference {
    GroupCreationRequest request;
    String childDbRef;
    String currentNodeDbRef;
    String rootId;

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public ChildNodeWithDBReference()
    {

    }
    public ChildNodeWithDBReference(GroupCreationRequest request
            , String dbRef, String currentNodeDbRef)
    {
        this.request = request;
        this.childDbRef = dbRef;
        this.currentNodeDbRef = currentNodeDbRef;
    }

    public GroupCreationRequest getRequest() {
        return request;
    }

    public void setRequest(GroupCreationRequest request) {
        this.request = request;
    }

    public String getChildDbRef() {
        return childDbRef;
    }

    public void setChildDbRef(String childDbRef) {
        this.childDbRef = childDbRef;
    }

    public String getCurrentNodeDbRef() {
        return currentNodeDbRef;
    }

    public void setCurrentNodeDbRef(String currentNodeDbRef) {
        this.currentNodeDbRef = currentNodeDbRef;
    }

}
