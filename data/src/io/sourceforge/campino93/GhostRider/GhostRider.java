package io.sourceforge.campino93.GhostRider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class GhostRider extends JavaPlugin
{
	private int x1,x2,  y1,y2,  z1,z2;
	int direction;
	Player owner;
	List<Ships> shipList = new ArrayList<Ships>();

	  public void onEnable()
	  {
		  File d = new File(".\\plugins\\ghostrider");
		  if (!d.exists()) d.mkdir();
		
		  d = new File(".\\plugins\\ghostrider\\saves");
		  if (!d.exists()) d.mkdir();
		
		  read_shipList();
	  }
	
	  public void onDisable()
	  {
	  }
	
	  public void read_shipList()
	  {
	    File d = new File("plugins\\ghostrider\\saves\\");
	    String[] dirList = d.list();
	
	    for (int i = 0; i < dirList.length; i++)
	    {
	      File f = new File("plugins\\ghostrider\\saves\\" + dirList[i]);
	      String[] fileList = f.list();
	
	      for (int j = 0; j < fileList.length; j++)
	      {
	        String p = dirList[i];
	        String[] shipName = fileList[j].split("\\.");
	
	        Ships ship = new Ships(p, shipName[0], this);
	        this.shipList.add(ship);
	        writeIndexOfShip(shipName[0], this.shipList.size() - 1, p);
	      }
	    }
	  }
	
	  public void writeShipToFile(String Shipname)
	  {
	    String path = "plugins\\ghostrider\\saves\\" + this.owner.getName();
	
	    File file = new File(path);
	    if (!file.exists()) file.mkdir();
	    try
	    {
	      file = new File(path + "\\" + Shipname + ".ghostrider");
	      FileWriter writer = new FileWriter(file);
	
	      writer.write("P1:" + this.x1 + ";" + this.y1 + ";" + this.z1 + "\r\n");
	      writer.write("P2:" + this.x2 + ";" + this.y2 + ";" + this.z2 + "\r\n");
	      writer.write("Dir:" + this.direction + "\r\n");
	
	      writer.flush();
	
	      writer.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	  }
	
	  public int readIndexOfShip(String Shipname, Player p) {
	    try {
	      BufferedReader in = new BufferedReader(new FileReader("plugins\\ghostrider\\saves\\" + p.getName() + "\\" + Shipname + ".ghostrider"));
	
	      in.readLine();
	      in.readLine();
	      in.readLine();
	
	      String[] buff = in.readLine().split(":");
	
	      if (buff[0].equals("Index")) {
	        in.close();
	        return Integer.parseInt(buff[1]);
	      }
	      in.close();
	      return -1;
	    }
	    catch (IOException e) {
	      e.printStackTrace();
	    }return -1;
	  }
	
	  public void writeIndexOfShip(String Shipname, int i, String p)
	  {
	    String P1 = ""; String P2 = ""; String Dir = "";
	    try
	    {
	      BufferedReader in = new BufferedReader(new FileReader("plugins\\ghostrider\\saves\\" + p + "\\" + Shipname + ".ghostrider"));
	
	      P1 = in.readLine();
	      P2 = in.readLine();
	      Dir = in.readLine();
	
	      in.close();
	    }
	    catch (IOException e) {
	      e.printStackTrace();
	    }
	
	    File f = new File("plugins\\ghostrider\\saves\\" + p + "\\" + Shipname + ".ghostrider");
	    try
	    {
	      FileWriter writer = new FileWriter(f);
	      writer.write(P1 + "\r\n");
	      writer.write(P2 + "\r\n");
	      writer.write(Dir + "\r\n");
	      writer.write("Index:" + i);
	
	      writer.flush();
	      
	      writer.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	  }
	
	  public int getCountOfShips(Player owner)
	  {
	    File f = new File("plugins\\ghostrider\\saves\\" + owner.getName());
	    if (f.exists()) return f.list().length;
	    return -1;
	  }
	
	  public void initShip(Player p, String Shipname, int Length, int Heigth, int Width)
	  {
	    x1 = p.getLocation().getBlockX();
	    y1 = (p.getLocation().getBlockY() - 1);
	    z1 = p.getLocation().getBlockZ();
	
	    direction = getPlayerDirection(owner);
	
	    y2 = (y1 + Heigth);
	
	    switch (direction) {
	    case 0:
	      x2 = (x1 + Width);
	      z2 = (z1 + Length);
	      break;
	    case 1:
	      x2 = (x1 - Length);
	      z2 = (z1 + Width);
	      break;
	    case 2:
	      x2 = (x1 - Width);
	      z2 = (z1 - Length);
	      break;
	    case 3:
	      x2 = (x1 + Length);
	      z2 = (z1 - Width);
	    }
	
	    writeShipToFile(Shipname);
	
	    owner.sendMessage("x1,y1,z1:" + x1 + "," + y1 + "," + z1);
	    owner.sendMessage("x2,y2,z2:" + x2 + "," + y2 + "," + z2);
	
	    owner.sendMessage("Dir:" + direction);
	    
	    owner.sendMessage("Ship " + Shipname + " initialized.");
	    owner.sendMessage("Use '/startShip " + Shipname + "' to use the ship.");
	  }
	
	  public int getPlayerDirection(Player p)
	  {
	    float fAdd = 0;
	    float tmp = p.getLocation().getYaw();
	
	    p.sendMessage(""+ tmp);
	    
	    tmp += fAdd;
	    int idirection = (int)tmp;
	
	    while (true)
	    {
	      if ((idirection >= 315) || ((idirection < 45) && (idirection >= 0))) return 2;
	      if (idirection >= 225) return 1;
	      if (idirection >= 135) return 0;
	      if (idirection >= 45) return 3;
	      idirection += 360;
	    }
	  }
	
	  public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args)
	  {
	    int cntArgs = args.length;
	
	    this.owner = ((Player)sender);
	
	    if (cmd.getName().equalsIgnoreCase("test"))
	    {
	    	
	    }
	
	    // ####################### INIT SHIP #######################
	    
	    if (cmd.getName().equalsIgnoreCase("initShip"))
	    {
	      String Shipname = args[0];
	
	      if (cntArgs == 4) {
	        initShip(this.owner, Shipname, Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
	      } else {
	        ((Player)sender).sendMessage("Wrong count of parameters");
	        return false;
	      }
	
	      Ships ship = new Ships(this.owner, Shipname, this);
	      ship.setSize(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
	
	      this.shipList.add(ship);
	      int i = this.shipList.size() - 1;
	
	      writeIndexOfShip(Shipname, i, this.owner.getName());
	    }
	
	    // ####################### START SHIP #######################
	    
	    if (cmd.getName().equalsIgnoreCase("startShip"))
	    {
	      if ((cntArgs >= 1) && (cntArgs <= 2))
	      {
	        int i = readIndexOfShip(args[0], this.owner);
	        Ships ship = (Ships)this.shipList.get(i);
	        ship.readShip();
	        ship.getWaterLvl();
	        ship.driving = true;
	
	        if ((cntArgs == 2) && (args[1].equals("debug")))
	        {
	          ship.driveShip(true);
	        }
	        else ship.driveShip(false);
	
	        this.owner.sendMessage("Start driving");
	      } else {
	        ((Player)sender).sendMessage("Wrong count of parameters");
	      }
	
	    }
	
	    // ####################### STOP SHIP #######################
	    
	    if (cmd.getName().equalsIgnoreCase("stopShip"))
	    {
	      int i = readIndexOfShip(args[0], this.owner);
	
	      this.owner.sendMessage("Stop driving");
	
	      if (cntArgs == 1) {
	        Ships ship = (Ships)this.shipList.get(i);
	
	        ship.driving = false;
	        ship.writeShipToFile(args[0]);
	        ship = null;
	      }
	    }
	
	    // ####################### SET DIR SHIP #######################
	    if (cmd.getName().equalsIgnoreCase("setDirShip"))
	    {
	      if (cntArgs == 2) {
	        String shipName = args[0];
	        int dir = Integer.parseInt(args[1]);
	        
	        int i = readIndexOfShip(shipName, this.owner);
	        Ships ship = (Ships)this.shipList.get(i);
	        
	        ship.setDirection(dir);
	        ship = null;
	      }
	    }
	    
	    // ####################### TURN SHIP #######################
	    
	    if (cmd.getName().equalsIgnoreCase("turnShip"))
	    {
	      if (cntArgs == 2) {
	        String shipName = args[0];
	        String dir = args[1];
	        
	        int i = readIndexOfShip(shipName, this.owner);
	        Ships ship = (Ships)this.shipList.get(i);
	        
	        ship.turnShip(dir);
	        ship = null;
	      } else {
	    	  
	      }
	    }

	    return true;
	    
  }
}
