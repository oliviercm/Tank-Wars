package tankrotationexample.game;

public interface Damageable {
    int getHealth();
    void setHealth(int health);
    boolean takeDamage(int damage);
}
