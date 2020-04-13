package entity;

/**
 * @ClassName: role
 * @Description:
 * @Author: lixl
 * @Date: 2020/4/7 22:37
 */
public class Role {

    private int id;

    private String roleName;

    private int power;

    public Role() {}
    public Role(int power) {
        this.power = power;
    }

    public void init() {
        System.out.println("role init method");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    @Override
    public String toString() {
        return "role{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                ", power=" + power +
                '}';
    }
}
