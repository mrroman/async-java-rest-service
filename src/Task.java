import javax.xml.bind.annotation.*;

@XmlRootElement(name = "task")
@XmlAccessorType(XmlAccessType.FIELD)
class Task {

    private String id;
    private String title;
    private boolean checked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
