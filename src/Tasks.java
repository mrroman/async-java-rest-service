import java.util.List;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "tasks")
@XmlAccessorType(XmlAccessType.FIELD)
class Tasks {

    @XmlElement(name = "task")
    private List<Task> tasks;

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
