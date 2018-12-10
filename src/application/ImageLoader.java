package application;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

import javafx.scene.image.Image;

public class ImageLoader {
	
	public static Image[] explosion = new Image[24];
	public static Image lit;
	public static Image unlit;
	
	public static File stage0Text;
	public static Image stage0Image;
	
	public static Image asciiWhite;
	public static Image asciiGray;
	public static Image asciiBlack;
	
	public static Image baseHover;
	public static Image baseIdle;
	public static Image blankpane;
	public static Image callDisable;
	public static Image callIdle;
	public static Image callHover;
	public static Image channellingDisable;
	public static Image channellingIdle;
	public static Image channellingHover;
	public static Image dirtBackground;
	public static Image hangingSign;
	public static Image iconEmerald;
	public static Image iconEnemy;
	public static Image iconHeart;
	public static Image upgradeHover;
	public static Image upgradeIdle;
	public static Image upgradeArcheryDisable;
	public static Image upgradeArcheryIdle;
	public static Image upgradeArcheryHover;
	public static Image upgradeWitchDisable;
	public static Image upgradeWitchIdle;
	public static Image upgradeWitchHover;
	public static Image upgradeArtilleryDisable;
	public static Image upgradeArtilleryIdle;
	public static Image upgradeArtilleryHover;
	public static Image saleDisable;
	public static Image saleIdle;
	public static Image saleHover;
	
	public static Image potion;
	public static Image[] splash = new Image[24];
	
	public static Image[] nullTower = new Image[2];
	public static Image[][] archeryTower = new Image[4][4];
	public static Image[] witchTower = new Image[4];
	public static Image[][] artilleryTower = new Image[4][2];
	
	public static Image[] zombie0 = new Image[21];
	public static Image[] zombie1 = new Image[21];
	public static Image[] zombie2 = new Image[21];
	public static Image[] zombie3 = new Image[21];
	public static Image[] zombie4 = new Image[21];
	
	public static Image[] husk0;
	public static Image[] husk1;
	public static Image[] husk2;
	public static Image[] husk3;
	public static Image[] husk4;
	
	public static Image[] skeleton0;
	public static Image[] skeleton1;
	public static Image[] skeleton2;
	public static Image[] skeleton3;
	public static Image[] skeleton4;
	
	public static Image[] creeper0;
	public static Image[] creeper1;
	public static Image[] creeper2;
	public static Image[] creeper3;
	public static Image[] creeper4;
	
	static {
		
		/*
		 = new Image(ClassLoader.getSystemResource("").toString());
		*/
		
		for (int i = 0; i < 24; i++) {
			explosion[i] = new Image(loader("explosion/explosion(" + (i + 1) + ").png"));
		}
		lit = new Image(loader("explosion/lit.png"));
		unlit = new Image(loader("explosion/unlit.png"));
		
		//stage0Text = new File(loader("stage/0/stage.txt"));
		//stage0Image = new Image(loader("stage/0/stage.png"));
		
		asciiWhite = new Image(loader("font/ascii_white.png"));
		asciiGray = new Image(loader("font/ascii_gray.png"));
		asciiBlack = new Image(loader("font/ascii_black.png"));
		
		callDisable = new Image(loader("gui/call_disable.png"));
		callIdle = new Image(loader("gui/call_idle.png"));
		callHover = new Image(loader("gui/call_hover.png"));
		channellingDisable = new Image(loader("gui/channelling_disable.png"));
		channellingIdle = new Image(loader("gui/channelling_idle.png"));
		channellingHover = new Image(loader("gui/channelling_hover.png"));
		dirtBackground = new Image(loader("gui/dirtbackground.png"));
		hangingSign = new Image(loader("gui/hangingsign.png"));
		blankpane = new Image(loader("gui/blankpanel.png"));
		iconEmerald = new Image(loader("gui/icon_emerald.png"));
		iconEnemy = new Image(loader("gui/icon_enemy.png"));
		iconHeart = new Image(loader("gui/icon_heart.png"));
		upgradeArcheryDisable = new Image(loader("gui/upgrade_archery_disable.png"));
		upgradeArcheryIdle = new Image(loader("gui/upgrade_archery_idle.png"));
		upgradeArcheryHover = new Image(loader("gui/upgrade_archery_hover.png"));
		upgradeWitchDisable = new Image(loader("gui/upgrade_witch_disable.png"));
		upgradeWitchIdle = new Image(loader("gui/upgrade_witch_idle.png"));
		upgradeWitchHover = new Image(loader("gui/upgrade_witch_hover.png"));
		upgradeArtilleryDisable = new Image(loader("gui/upgrade_artillery_disable.png"));
		upgradeArtilleryIdle = new Image(loader("gui/upgrade_artillery_idle.png"));
		upgradeArtilleryHover = new Image(loader("gui/upgrade_artillery_hover.png"));
		saleDisable = new Image(loader("gui/upgrade_sell_disable.png"));
		saleIdle = new Image(loader("gui/upgrade_sell_idle.png"));
		saleHover = new Image(loader("gui/upgrade_sell_hover.png"));
		
		potion = new Image(loader("potion/potion.png"));
		for (int i = 0; i < 24; i++) {
			splash[i] = new Image(loader("potion/splash(" + (i + 1) + ").png"));
		}
		
		nullTower[0] = new Image(loader("tower/null0.png"));
		nullTower[1] = new Image(loader("tower/null1.png"));
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				archeryTower[i][j] = new Image(loader("tower/stray" + i + "" + j + ".png"));
			}
			witchTower[i] = new Image(loader("tower/witch" + i + ".png"));
			for (int j = 0; j < 2; j++) {
				artilleryTower[i][j] = new Image(loader("tower/tnt" + i + "" + j + ".png"));
			}
		}
		
		for (int i = 0; i < 21; i++) {
			
		}
		
	}
	
	public static String loader(String path) {
		return ClassLoader.getSystemResource(path).toString();
	}

}
