package io.sourceforge.campino93.GhostRider;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class GhostRider extends JavaPlugin {
	private boolean driving = false;
	int x1,x2, y1,y2, z1,z2;
	Player owner;
	
	@Override
	public void onEnable()
	{
		getLogger().info("Start");
		
	}
	
	public void initShip(Player p, int type)
	{
		owner = p;
		
		if (type == 1)
		{
			x1 = p.getLocation().getBlockX();
			y1 = p.getLocation().getBlockY()-1;
			z1 = p.getLocation().getBlockZ();
			owner.sendMessage("& now set the second place");
		} else if (type == 2) {
			x2 = p.getLocation().getBlockX();
			y2 = p.getLocation().getBlockY()-1;
			z2 = p.getLocation().getBlockZ();
			
			
			owner.sendMessage("x1,y1,z1:"+x1+","+y1+","+z1);
			owner.sendMessage("x2,y2,z2:"+x2+","+y2+","+z2);
			
			owner.sendMessage("Ship initialized");
		}
	
		
	}
	
	public boolean driveShip(int debug)
	{
		if (!driving) return false;
		
		/*
		 * Code fürs fahren
		 * */
		owner.sendMessage(""+(Math.abs(x2-x1)));
		for (int y=0; y<=(Math.abs(y2-y1)); y++)
		{
			for (int x=0; x<=(Math.abs(x2-x1)); x++)
			{
				for (int z=0; z<=(Math.abs(z2-z1)); z++)
				{
					
					Block oldBlock = owner.getWorld().getBlockAt(x1+(x*(-1)), y1+y, z1+z);
					Block newBlock = owner.getWorld().getBlockAt(x1+(x*(-1))+1, y1+y, z1+z);
					int oldBlock_ID = oldBlock.getTypeId();
					
					owner.sendMessage("oldBlock:" + ", x:" + (x1+x) + ", z:" + (z1+z) + ", B_ID:"+oldBlock_ID);
					
					
					// Delete old Shipheck
					oldBlock.setType((oldBlock.getY() < 63) ? Material.WATER : Material.AIR);
					
					// Build new Shipfront
					newBlock.setTypeId(oldBlock_ID);
			
					// Debug Position
					if (debug == 1) owner.sendMessage("x:"+x + " ID:"+oldBlock_ID);
					
				}
			}
		}
		
		// Next Position
		x1++;
		x2++;
				
		// Delay
		Bukkit.getScheduler().scheduleSyncDelayedTask((GhostRider) this, new Runnable() {
			
			@Override
			public void run()
			{
				boolean tmp = driveShip(0);
			}
		}, 20L);
		
		// Compiler muss glücklich sein
		return true;
	}
	
	@Override
	public void onDisable()
	{
		getLogger().info("Stop");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args)
	{
		int cntArgs = args.length;

		if (cmd.getName().equalsIgnoreCase("initShip"))
		{
			// InitDrive aufrufen
			if (cntArgs == 1) {
				initShip((Player)sender, Integer.parseInt(args[0]));
			} else {
				((Player)sender).sendMessage("Wrong count of parameters");
			}
			
		}
		
		if (cmd.getName().equalsIgnoreCase("startShip"))
		{ 
			// Start Driving
			owner.sendMessage("Start driving");
			
			driving = true;
			
			if (cntArgs > 0)
			{
				if (args[0].equals("debug")) driveShip(1);
			} else {
				driveShip(0);
			}
			
		}
		
		if (cmd.getName().equalsIgnoreCase("stopShip"))
		{
			// Stop Driving
			owner.sendMessage("Stop driving");
						
			driving = false;
		}
		
		return true;
	}
}
