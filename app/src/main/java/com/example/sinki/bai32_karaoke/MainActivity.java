package com.example.sinki.bai32_karaoke;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sinki.adapter.MusicAdapter;
import com.example.sinki.model.BaiHat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lvBaiHatGoc;
    ArrayList<BaiHat>dsBaiHatGoc;
    ArrayList<BaiHat>dsBaiHatGocHienThi;
    MusicAdapter adapterBaiHatGoc;

    ListView lvBaiHatYeuThich;
    ArrayList<BaiHat>dsBaiHatYeuThich;
    ArrayList<BaiHat>dsBaiHatYeuThichHienThi;
    MusicAdapter adapterBaiHatYeuThich;

    TabHost tabHost;

    public static String DATABASE_NAME="Arirang.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database=null;

    public static  String lasteTabSelected = "t1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xuLySaoChepCSDLTuAssetsVaoHeThongMobile();

        addControls();
        addEvents();
    }

    private void xuLySaoChepCSDLTuAssetsVaoHeThongMobile() {
        //private app
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists())    {
            try
            {
                CopyDataBaseFromAsset();
                Toast.makeText(this, "Copying sucess from Assets folder", Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void CopyDataBaseFromAsset() {
        try
        {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);

            // Path to the just created empty db
            String outFileName = layDuongDanLuuTru();

            // if the path doesn't exist first, create it
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
            {
                f.mkdir();
            }
            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
        catch (Exception ex)
        {
            Log.e("Loi sao chep",ex.toString());
        }
    }

    private String layDuongDanLuuTru()
    {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX+ DATABASE_NAME;
    }

    private void addEvents() {
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String id) {
                if(id.equalsIgnoreCase("t1"))
                {
                    lasteTabSelected = "t1";
                    xuLyHienThiBaiHatGoc();
                }
                if(id.equalsIgnoreCase("t2"))
                {
                    lasteTabSelected = "t2";
                    xuLyHienThiBaiHatYeuThich();
                }
            }
        });

    }

    private void xuLyHienThiBaiHatGoc() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.query("ArirangSongList", null, null, null, null, null, null);
        dsBaiHatGoc.clear();
        dsBaiHatGocHienThi.clear();
        while (cursor.moveToNext())
        {
            String mabh=cursor.getString(0);
            String ten = cursor.getString(1);
            String caSi = cursor.getString(3);
            int yeuThich = cursor.getInt(5);
            BaiHat music = new BaiHat();
            music.setMa(mabh);
            music.setTen(ten);
            music.setCaSi(caSi);
            music.setLike(yeuThich==1);
            dsBaiHatGoc.add(music);
            dsBaiHatGocHienThi.add(music);
        }
        cursor.close();
        adapterBaiHatGoc.notifyDataSetChanged();

    }

    private void xuLyHienThiBaiHatYeuThich() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.query("ArirangSongList", null, "YEUTHICH =?", new String[]{"1"}, null, null, null);
        dsBaiHatYeuThich.clear();
        dsBaiHatYeuThichHienThi.clear();
        while (cursor.moveToNext())
        {
            String mabh=cursor.getString(0);
            String ten = cursor.getString(1);
            String caSi = cursor.getString(3);
            int yeuThich = cursor.getInt(5);
            BaiHat music = new BaiHat();
            music.setMa(mabh);
            music.setTen(ten);
            music.setCaSi(caSi);
            music.setLike(yeuThich==1);
            dsBaiHatYeuThich.add(music);
            dsBaiHatYeuThichHienThi.add(music);
        }
        cursor.close();
        adapterBaiHatYeuThich.notifyDataSetChanged();
    }

    private void addControls() {
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("t1");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator("",getResources().getDrawable(R.drawable.music));
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("t2");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator("",getResources().getDrawable(R.drawable.favoritemusic));
        tabHost.addTab(tab2);

        lvBaiHatGoc = (ListView) findViewById(R.id.lvBaiHatGoc);
        dsBaiHatGoc = new ArrayList<>();
        dsBaiHatGocHienThi = new ArrayList<>();
        adapterBaiHatGoc = new MusicAdapter(MainActivity.this,R.layout.item,dsBaiHatGocHienThi);
        lvBaiHatGoc.setAdapter(adapterBaiHatGoc);

        lvBaiHatYeuThich = (ListView) findViewById(R.id.lvBaiHatYeuThich);
        dsBaiHatYeuThich = new ArrayList<>();
        dsBaiHatYeuThichHienThi = new ArrayList<>();
        adapterBaiHatYeuThich = new MusicAdapter(MainActivity.this,R.layout.item,dsBaiHatYeuThichHienThi);
        lvBaiHatYeuThich.setAdapter(adapterBaiHatYeuThich);
        lvBaiHatGoc.setTextFilterEnabled(true);

        xuLyHienThiBaiHatGoc();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search,menu);
        MenuItem item = menu.findItem(R.id.mnuSearch);
        SearchView mnuSearch = (SearchView) item.getActionView();

        mnuSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(lasteTabSelected == "t1")
                {
                    xuLySearchBaiHat(dsBaiHatGoc, dsBaiHatGocHienThi, s,adapterBaiHatGoc);
//                    adapterBaiHatGoc.getFilter().filter(s);
                }
                if(lasteTabSelected == "t2")
                {
                    xuLySearchBaiHat(dsBaiHatYeuThich,dsBaiHatYeuThichHienThi,s,adapterBaiHatYeuThich);
//                    MainActivity.this.adapterBaiHatYeuThich.getFilter().filter(s);
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void xuLySearchBaiHat(ArrayList<BaiHat> dsBaiHatGoc, ArrayList<BaiHat> dsBaiHatHienThi, String search, MusicAdapter adapter) {
        search = search.toLowerCase();
        dsBaiHatHienThi.clear();
        if(search != null && search.length()>0)
        {
            for (BaiHat bh:dsBaiHatGoc)
            {
                if (bh.getTen().toLowerCase().startsWith(search))
                    dsBaiHatHienThi.add(bh);
            }
        }
        else
        {
            for (BaiHat bh:dsBaiHatGoc)
                dsBaiHatHienThi.add(bh);
        }
        adapter.notifyDataSetChanged();
    }
}
