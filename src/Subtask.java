public class Subtask extends Task {

    private int subId;

    public Subtask(String name, String description, TaskStatus status) {
        super(name, description, status);
    }

    public int getSubId() {
        return subId;
    }

    public void setSubId(int subId) {
        this.subId = subId;
    }

    @Override
    public String toString() {
        String result = "Subtask{" +
                "name='" + getName() + '\'' +
                ", id=" + subId;
        if (getDescription() != null) {
            result = result + ", description.length=" + getDescription().length();
        } else {
            result = result + ", description=null";
        }

        return result + ", status=" + getStatus() + '}';
    }

}
