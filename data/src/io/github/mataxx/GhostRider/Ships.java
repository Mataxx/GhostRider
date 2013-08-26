package io.github.mataxx.GhostRider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Ships
{
  private int x1,x2,  y1,y2,  z1,z2;
  private int length, width, height;
  private Player owner;
  public boolean driving;
  private int direction;
  public int shipIndex;
  GhostRider plugin;
  private String Shipname;
  private String owner_name;
  private int waterlvl = 0;

  public Ships(Player p, String Shipname, GhostRider plug) {
    this.owner = p;
    this.owner_name = p.getName();
    this.plugin = plug;
    this.Shipname = Shipname;
  }

  public Ships(String p, String Shipname, GhostRider plug) {
    this.owner_name = p;
    this.plugin = plug;
    this.Shipname = Shipname;
  }
  public void readShip() {
    readShip(this.Shipname);
  }
  
  public void setSize(int l, int h, int w) {
	  this.length = l;
	  this.height = h;
	  this.width = w;
  }

  public void swap_xz() {
    int tmp = this.z1;
    this.z1 = this.z2;
    this.z2 = tmp;
    tmp = this.x1;
    this.x1 = this.x2;
    this.x2 = tmp;
  }

  public boolean getWaterLvl()
  {
	  for (int i = this.y1; i <= this.y2; i++)
	  {
		  switch (direction)
		  {
		  case 0:
			  if (Bukkit.getWorld("world").getBlockAt(this.x1 - 1, this.y1 + i, this.z1).getType() != Material.WATER) this.waterlvl = i;
			  return false;
			  
		  case 1:
			  if (Bukkit.getWorld("world").getBlockAt(this.x1, this.y1 + i, this.z1 - 1).getType() != Material.WATER) this.waterlvl = i;
			  return false;
			  
		  case 2:
			  if (Bukkit.getWorld("world").getBlockAt(this.x1 + 1, this.y1 + i, this.z1).getType() != Material.WATER) this.waterlvl = i;
			  return false;
			  
		  case 3:
			  if (Bukkit.getWorld("world").getBlockAt(this.x1, this.y1 + i, this.z1 + 1).getType() != Material.WATER) this.waterlvl = i;
			  return false;
			  
		  }
	  } 
	  return false;
  }
  
  public boolean driveShip(boolean debug)
  {
	  if (!this.driving) return false;
	  if (Bukkit.getPlayer(this.owner_name).isOnline()) {
		  this.owner = Bukkit.getPlayer(this.owner_name);
	  }

	  modifyShip(x1, x2, z1, z2, y1, y2, direction, 0, null);
	  
	  switch (this.direction) {
	  	case 0:
	  		this.z1--;
	  		this.z2--;
	  		break;
	  	case 1:
	  		this.x1++;
	  		this.x2++;
	  		break;
	  	case 2:
	  		this.z1++;
	  		this.z2++;
	  		break;
	  	case 3:
	  		this.x1--;
	  		this.x2--;
	  		break;
	  }

	  Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() { public void run() { Ships.this.driveShip(false); }} , 20L);
	  
	  return true;
  }
  
  public int[][][] modifyShip(int x1, int x2, int z1, int z2, int y1, int y2, int dir, int mode, int[][][] newStack){
	  /*
	   * mode 0: set next pos
	   * mode 1: write to stack
	   * mode 2: delete from world
	   * mode 3: set new pos
	   */
	  
	  int x_dir=0, z_dir=0, x_manu=0, z_manu=0;
	  int x1_tmp=0, z1_tmp=0;

	  switch (dir) {
	  	case 0:	
	  		x1_tmp = Math.min(x1, x2);
	  		z1_tmp = Math.min(z1, z2);
	  		x2 = Math.max(x1, x2);
	  		z2 = Math.max(z1, z2);
	  		x1 = x1_tmp;
	  		z1 = z1_tmp;
	
	  		x_dir = 0;
	  		x_manu = 1;
	
	  		z_dir = -1;
	  		z_manu = 1;
	  		break;
      
	  	case 1:
	  		x1_tmp = Math.max(x1, x2);
	  		z1_tmp = Math.min(z1, z2);
	  		x2 = Math.min(x1, x2);
	  		z2 = Math.max(z1, z2);
	  		x1 = x1_tmp;
	  		z1 = z1_tmp;
	  		
	  		x_dir = 1;
	  		x_manu = -1;
	  		
	  		z_dir = 0;
	  		z_manu = 1;
	  		break;
      
	  	case 2:
	  		x1_tmp = Math.max(x1, x2);
	  		z1_tmp = Math.max(z1, z2);
	  		x2 = Math.min(x1, x2);
	  		z2 = Math.min(z1, z2);
	  		x1 = x1_tmp;
	  		z1 = z1_tmp;
	  		
	  		x_dir = 0;
	  		x_manu = -1;
	  		
	  		z_dir = 1;
	  		z_manu = -1;
	  		break;
      
	  	case 3:
	  		x1_tmp = Math.min(x1, x2);
	  		z1_tmp = Math.max(z1, z2);
	  		x2 = Math.max(x1, x2);
	  		z2 = Math.min(z1, z2);
	  		x1 = x1_tmp;
	  		z1 = z1_tmp;
	  		
	  		x_dir = -1;
	  		x_manu = 1;
      
	  		z_dir = 0;
	  		z_manu = -1;
	  }
	  
	  
	  int[][][] stack = new int[20][20][20];	  	 
	  if (newStack != null) stack = newStack;

	  for (int h = 0; h <= height; h++)
	  {
		  for (int l = 0; l <= length; l++)
		  {	
			  for (int w = 0; w <= width; w++) {
				  int w2 = 0; int l2 = 0;
				  if ((dir == 1) || (dir == 3)) {
					  w2 = l;
					  l2 = w;
				  } else {
					  w2 = w;
					  l2 = l;
				  }

				  // normal driving
				  if(mode == 0) {
					  Block oldBlock = owner.getWorld().getBlockAt(x1 + w2 * x_manu, y1 + h, z1 + l2 * z_manu);
					  Block newBlock = owner.getWorld().getBlockAt(x1 + w2 * x_manu + x_dir, y1 + h, z1 + l2 * z_manu + z_dir);
					  int oldBlock_ID = oldBlock.getTypeId();
					  
					  oldBlock.setType(oldBlock.getY() <= this.waterlvl ? Material.WATER : Material.AIR);

					  newBlock.setTypeId(oldBlock_ID);
				  }
				  
				  // write ship to stack
				  if(mode == 1){
					  stack[h][l][w] = owner.getWorld().getBlockTypeIdAt(x1 + w2 * x_manu, y1 + h, z1 + l2 * z_manu);
					  writeLog(h+"-"+l+"-"+w+": "+stack[h][l][w]);
				  }
							  
				  // delete ship from world
				  if(mode == 2) {
					  int newBlockID = ((y1+h) <= waterlvl) ? Material.WATER.getId() : Material.AIR.getId();
					  Block deleteBlock = owner.getWorld().getBlockAt(x1 + w2 * x_manu, y1 + h, z1 + l2 * z_manu);
					  deleteBlock.setTypeId(newBlockID);					  
				  }

				  // ReadPos from Stack
				  if(mode == 3) {
					  
					  owner.sendMessage(dir +"");
					  Block newBlock = owner.getWorld().getBlockAt(x1 + w2 * x_manu, y1 + h, z1 + l2 * z_manu);
					  int newBlockID = stack[h][l][w];
					  
					  newBlock.setTypeId(newBlockID);
					  
					  writeLog(h+"-"+l+"-"+w+": "+stack[h][l][w]);
				  }				  
			  }
		  }
	  }
	  return (mode==1) ? stack : null;
  }
  
  public void turnShip(String cmdDir)
  {
	  int newDir=0;
	  int nx1=x1,nx2=x2,  nz1=z1,nz2=z2;
	  
	  switch (direction) {
	  	case 0:
	  		if (cmdDir.equals("left")) {
	  			newDir = 3;
	  			nx1 = x1-length+width;
	  			nz1 = z1+width;
	  		} else {
	  			newDir = 1;
	  			nx1 = x1+length;
	  		}
	  		break;
	  	case 1:
	  		if (cmdDir.equals("left")) {
	  			newDir = 0;
	  			nx1 = x1-width;
	  			nz1 = z1-length+width;
	  		} else {
	  			newDir = 2;
	  			nz1 = z1+length;
	  		}
	  		break;
	  	case 2:
	  		if (cmdDir.equals("left")) {
	  			newDir = 1;
	  			nx1 = x1+length-width;
	  			nz1 = z1-width;
	  		} else {
	  			newDir = 3;
	  			nx1 = x1-length;
	  		}
	  		break;
	  	case 3:
	  		if (cmdDir.equals("left")) {
	  			newDir = 2;
	  			nx1 = x1+width;
	  			nz1 = z1+length-width;
	  		} else {
	  			newDir = 0;
	  			nz1 = z1-length;
	  		}
	  		break;
	  }
	  
	  switch(newDir)
	  {
	  case 0:
	  		nx2 = nx1 + length;
			nz2 = nz1 + width;
	  		break;
	  case 1:
		  	nx2 = nx1 - length;
			nz2 = nz1 + width;
	  		break;
	  case 2:
		  	nx2 = nx1 - length;
			nz2 = nz1 - width;
	  		break;
	  case 3:
		  	nx2 = nx1 + length;
			nz2 = nz1 - width;
	  		break;
	  }
	  
	  // write ship to stack;
	  int[][][] blockIDs = new int[20][20][20];
	  blockIDs = modifyShip(x1, x2, z1, z2, y1, y2, direction, 1, null);
	  
	  // delete old ship from world
	  modifyShip(x1, x2, z1, z2, y1, y2, direction, 2, null);
	  
	  // set new ship position
	  owner.sendMessage("NEUE POSITION:"+nx1+"-"+nx2+"-"+nz1+"-"+nz2);
	  modifyShip(nx1, nx2, nz1, nz2, y1, y2, newDir, 3, blockIDs);
	  
	  // set new shipstats global
	  direction = newDir;
	  x1 = nx1;
	  z1 = nz1;
	  x2 = nx2;
	  z2 = nz2;
	  
	  // writeShip to file
	  this.writeShipToFile(Shipname);
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
      writer.write("Index:" + this.shipIndex);

      writer.flush();

      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean readShip(String Shipname) {
    try {
      BufferedReader in = new BufferedReader(new FileReader("plugins\\ghostrider\\saves\\" + this.owner_name + "\\" + Shipname + ".ghostrider"));

      String[] buff = in.readLine().split(":");
      if (buff[0].equals("P1")) {
        String[] coods = buff[1].split(";");
        this.x1 = Integer.parseInt(coods[0]);
        this.y1 = Integer.parseInt(coods[1]);
        this.z1 = Integer.parseInt(coods[2]);
      } else {
        in.close();
        return false;
      }
      String[] coods;
      buff = in.readLine().split(":");
      if (buff[0].equals("P2")) {
        coods = buff[1].split(";");
        this.x2 = Integer.parseInt(coods[0]);
        this.y2 = Integer.parseInt(coods[1]);
        this.z2 = Integer.parseInt(coods[2]);
      } else {
        in.close();
        return false;
      }

      buff = in.readLine().split(":");
      if (buff[0].equals("Dir")) {
        this.direction = Integer.parseInt(buff[1]);
      } else {
        in.close();
        return false;
      }

      buff = in.readLine().split(":");
      if (buff[0].equals("Index")) {
        this.shipIndex = Integer.parseInt(buff[1]);
      } else {
        in.close();
        return false;
      }

      in.close();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  public void setDirection(int dir)
  {
    this.direction = dir;
  }
  
  public void writeLog(String var)
  {
	  Calendar cal = Calendar.getInstance();
	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:ms --> ");
	  String time = sdf.format(cal.getTime());
	  
	  try {
	  File f = new File(".\\plugins\\ghostrider\\debug.log");
	  FileWriter writer = new FileWriter(f, true);

      writer.write(time+ var + "\r\n");
      writer.flush();
      writer.close();
	  } catch (IOException e) {
    	  
      }
  }
}