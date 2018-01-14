package ru.samsung.itschool.dbgame;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayerActivity extends Activity {

	private int playerID;
	private DBManager dbManager;
	
	String userPics_path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		userPics_path = this.getExternalFilesDir(null)
		.getAbsolutePath() + "/userpics";
		
		Intent i = this.getIntent();
		playerID = (i.getExtras()).getInt("playerID");
		dbManager = new DBManager(this);

		showPlayerData();

	}

	private void showPlayerData() {
		String playerName = dbManager.getUserName(playerID);
		String playerPic = dbManager.getUserPic(playerID);
		TextView tv = (TextView) findViewById(R.id.playerName);
		tv.setText(playerName);
		if (!playerPic.equals(""))
		{
			ImageView userPic = (ImageView) this.findViewById(R.id.userPic);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bitmap = BitmapFactory.decodeFile(userPics_path+"/"+playerPic+".png", options);
			userPic.setImageBitmap(bitmap);
		}
	}

	static final int REQUEST_IMAGE_CAPTURE = 1;

	public void getPicture(View v) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			ImageView userPic = (ImageView) this.findViewById(R.id.userPic);
			userPic.setImageBitmap(imageBitmap);
			String pic = savePic(imageBitmap);
			dbManager.userUpdate(playerID, dbManager.getUserName(playerID), pic);
			
		}
	}

	private String savePic(Bitmap userpic) {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File dir = new File(userPics_path);
		if (!dir.exists()) dir.mkdirs();
		File file = new File(dir, timeStamp + ".png");
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(file);

			userpic.compress(Bitmap.CompressFormat.PNG, 85, fOut);
			fOut.flush();
			fOut.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "";
		}
        return timeStamp;
	}
}
