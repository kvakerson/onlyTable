package Table;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class Table implements Listener {
    //public static final Table instance = new Table();

    public static Location loc;
    protected static int rain_cooldown = 20;//from cfg
    protected static int killed_table_after = 40;
    protected static boolean isDestroyable = false;
    protected static boolean destoyedByPlayer;
    static public void toSpawnTable() {

        Random random = new Random();
        int max = 100;// change using cfg
        int min = -100;

        int x = (Math.abs(random.nextInt() % (max - min))) + min;
        int z = (Math.abs(random.nextInt() % (max - min))) + min;

        loc = new Location(Bukkit.getWorld("world"), x, 0, z);
        int maxHeight = loc.getWorld().getMaxHeight(), minHeight = 0;
        int highestBlock = -1;

        for (int i = maxHeight; i >= minHeight; i--) {
            Location bufLoc = new Location(Bukkit.getWorld("world"), x, i, z);

            if (bufLoc.getBlock().getType() != Material.AIR) {
                highestBlock = i;
                break;
            }
        }
        loc.setY(highestBlock + 1);
        loc.getBlock().setType(Material.ENCHANTMENT_TABLE);
        System.out.println("/n/n" + loc.getX() + " " + loc.getY() + " " + loc.getZ() + "/n/n");


    }

    static public boolean toDeleteTable() {
        if(loc.getBlock().getType()==Material.ENCHANTMENT_TABLE) {
            loc.getBlock().setType(Material.AIR);
            return (true);
        }
        return false;
    }

    @EventHandler
    public void leftClickingTable(PlayerInteractEvent event) {

        if (event.getAction() == Action.LEFT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE
                && Table.loc.distance(event.getClickedBlock().getLocation()) < 1) {
            event.setCancelled(true);
            //ChatUtil.sendMessage(event.getPlayer(),"Башку себе проломи, долбаеб",false );
            System.out.println("НЕ ЛОМАЙ ЛКМОМ");
        }
    }

    @EventHandler
    public void rightClickingTable(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE
                && Table.loc.distance(event.getClickedBlock().getLocation()) < 1) {
            event.setCancelled(true);
            //ChatUtil.sendMessage(event.getPlayer(),"Башку себе проломи, долбаеб",false );
            System.out.println("DESTROYABLE" + isDestroyable);
            if (isDestroyable) {
                System.out.println("!!!!!!DESTROYABLE" + isDestroyable);
                destoyedByPlayer = true;
                toDeleteTable();
            }

        }
    }


    static public void workingMeth() {
        new BukkitRunnable() {
            @Override
            public void run() {
                isDestroyable=false;
                destoyedByPlayer=false;

                toSpawnTable();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        isDestroyable = true;
                        System.out.println("da" + isDestroyable);
                    }
                }.runTaskLater(Kvakerson.instance, 20L * rain_cooldown);
                System.out.println("Выполнено условие на разрушение");

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // isDestroyable=true;
                        destoyedByPlayer=false;
                        if(toDeleteTable());
                           System.out.println("Selfdestroying");
                    }
                }.runTaskLater(Kvakerson.instance, 20L * killed_table_after);
                System.out.println("Выполнено условие на саморазрушение");
                int time=0;
                if(destoyedByPlayer){
                    time=rain_cooldown+0;
                }
                else {

                }

            }
        }.runTaskTimer(Kvakerson.instance, 20L * 5, 20L * 60);
    }

}
