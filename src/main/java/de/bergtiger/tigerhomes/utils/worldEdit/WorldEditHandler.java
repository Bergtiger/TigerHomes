package de.bergtiger.tigerhomes.utils.worldEdit;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import de.bergtiger.tigerhomes.bdo.Home;
import de.bergtiger.tigerhomes.dao.TigerConnection;
import de.bergtiger.tigerhomes.exception.IncompleteRegionException;
import de.bergtiger.tigerhomes.exception.NoRegionException;
import de.bergtiger.tigerhomes.exception.NoSQLConnectionException;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Handles WorldEdit methods for soft depend.
 */
public class WorldEditHandler {

	/**
	 *
	 * @param p whose selection is searched
	 * @param ignoreVertical true - complete height, false - selection only
	 * @return List containing all homes in the selection
	 * @throws NoSQLConnectionException
	 * @throws NoRegionException
	 * @throws IncompleteRegionException
	 */
	public static List<Home> getHomes(Player p, boolean ignoreVertical) throws NoSQLConnectionException, NoRegionException, IncompleteRegionException {
		if (p != null) {
			// get we player
			BukkitPlayer wePlayer = BukkitAdapter.adapt(p);
			if (WorldEdit.getInstance().getSessionManager().contains(wePlayer)) {
				LocalSession session = WorldEdit.getInstance().getSessionManager().get(wePlayer);
				try {
					Region region = session.getSelection(BukkitAdapter.adapt(p.getWorld()));
					BlockVector3 minPoint = region.getMinimumPoint();
					BlockVector3 maxPoint = region.getMaximumPoint();

					return TigerConnection.getHomeDAO().searchHomes(
							p.getWorld().getName(),
							minPoint.getX(),
							maxPoint.getX(),
							ignoreVertical ?   0 : minPoint.getY(),
							ignoreVertical ? 255 : maxPoint.getY(),
							minPoint.getZ(),
							maxPoint.getZ());
				} catch (com.sk89q.worldedit.IncompleteRegionException e) {
					// incomplete region
					throw new IncompleteRegionException();
				}
			} else {
				// no region
				throw new NoRegionException();
			}
		}
		return null;
	}
}
