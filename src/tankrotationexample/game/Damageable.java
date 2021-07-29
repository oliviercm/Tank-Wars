package tankrotationexample.game;

public interface Damageable {
    int getHealth();
    void setHealth(int health);
    void takeDamage(int damage);
}
