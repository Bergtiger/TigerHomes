package de.bergtiger.tigerhomes.bdo;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.time.LocalDateTime;

public class Home {

	private String uuid, name, w;
	private Double x,y,z;
	private Float yaw, pitch;
	private LocalDateTime t;

	/**
	 * Create an empty home.
	 */
	public Home() {}

	/**
	 * Create a home with this name at this location.
	 * @param name of this home
	 * @param loc of this home
	 */
	public Home(String uuid, String name, Location loc) {
		this.uuid = uuid;
		this.name = name;
		this.w = loc.getWorld().getName();
		this.x = loc.getX();
		this.y = loc.getY();
		this.z = loc.getZ();
		this.yaw = loc.getYaw();
		this.pitch = loc.getPitch();
	}

	/**
	 * Get uuid/owner of home.
	 * @return String
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Set uuid/owner of home.
	 * @param uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * Get name of home.
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name of home.
	 * @param name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get world name of home location.
	 * @return String
	 */
	public String getW() {
		return w;
	}

	/**
	 * Set world name of home location.
	 * @param w to set
	 */
	public void setW(String w) {
		this.w = w;
	}

	/**
	 * Get x position of home location.
	 * @return double
	 */
	public Double getX() {
		return x;
	}

	/**
	 * Set x position of home location.
	 * @param x to set
	 */
	public void setX(Double x) {
		this.x = x;
	}

	/**
	 * Get y position of home location.
	 * @return double
	 */
	public Double getY() {
		return y;
	}

	/**
	 * Set y position of home location.
	 * @param y to set
	 */
	public void setY(Double y) {
		this.y = y;
	}

	/**
	 * Get z position of home location.
	 * @return double
	 */
	public Double getZ() {
		return z;
	}

	/**
	 * Set z position of home location.
	 * @param z to set
	 */
	public void setZ(Double z) {
		this.z = z;
	}

	/**
	 * Get yaw of home location.
	 * @return float
	 */
	public Float getYaw() {
		return yaw;
	}

	/**
	 * Set yaw of home location.
	 * @param yaw to set
	 */
	public void setYaw(Float yaw) {
		this.yaw = yaw;
	}

	/**
	 * Get pitch of home location.
	 * @return float
	 */
	public Float getPitch() {
		return pitch;
	}

	/**
	 * Set pitch of home location.
	 * @param pitch to set
	 */
	public void setPitch(Float pitch) {
		this.pitch = pitch;
	}

	/**
	 * Get update time t of home.
	 * @return localdatetime
	 */
	public LocalDateTime getT() {
		return t;
	}

	/**
	 * Set update time t of home.
	 * @param t to set
	 */
	public void setT(LocalDateTime t) {
		this.t = t;
	}

	/**
	 * Get world of home location.
	 * @return world
	 */
	public World getWorld() {
		return Bukkit.getWorld(w);
	}

	/**
	 * Get home location.
	 * @return location
	 */
	public Location getLocation() {
		World world;
		if ((world = Bukkit.getWorld(w)) != null)
			return new Location(world, x, y, z, yaw, pitch);
		return null;
	}
}
