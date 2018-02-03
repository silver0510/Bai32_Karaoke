package com.example.sinki.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sinki.bai32_karaoke.MainActivity;
import com.example.sinki.bai32_karaoke.R;
import com.example.sinki.model.BaiHat;

import java.util.ArrayList;
import java.util.List;

import static com.example.sinki.bai32_karaoke.MainActivity.lasteTabSelected;
import static com.example.sinki.bai32_karaoke.R.id.btnDisLike;
import static com.example.sinki.bai32_karaoke.R.id.btnLike;
import static com.example.sinki.bai32_karaoke.R.id.txtMa;
import static com.example.sinki.bai32_karaoke.R.id.txtTenBaiHat;

/**
 * Created by Sinki on 8/25/2017.
 */

public class MusicAdapter extends ArrayAdapter<BaiHat> implements Filterable{
    Activity context;
    int resource;
    List<BaiHat> objects;
    List<BaiHat> dsFilter;
    LayoutInflater inflat;

//    private MusicFilter musicFilter = new MusicFilter();

    public MusicAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<BaiHat> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.dsFilter = objects;
        inflat = this.context.getLayoutInflater();
    }

//    @NonNull
//    @Override
//    public Filter getFilter() {
//        if(musicFilter == null)
//            musicFilter = new MusicFilter();
//        return musicFilter;
//    }

    private static class ViewHolder
    {
        TextView txtMa ;
        TextView txtCasi ;
        TextView txtTenBaiHat;
        ImageButton btnLike,btnDisLike;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final BaiHat music = this.dsFilter.get(position);
        final ViewHolder holder;
        if(convertView == null) {
            LayoutInflater inflat = LayoutInflater.from(getContext()); //this.context.getLayoutInflater();
            holder = new ViewHolder();
            convertView = inflat.inflate(this.resource, null);
            holder.txtMa = (TextView) convertView.findViewById(txtMa);
            holder.txtCasi = (TextView) convertView.findViewById(R.id.txtCaSi);
            holder.txtTenBaiHat = (TextView) convertView.findViewById(txtTenBaiHat);
            holder.btnLike = (ImageButton) convertView.findViewById(btnLike);
            holder.btnDisLike = (ImageButton) convertView.findViewById(btnDisLike);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtMa.setText(music.getMa());
        holder.txtTenBaiHat.setText(music.getTen());
        holder.txtCasi.setText(music.getCaSi());

        if(music.isLike())
            {
                holder.btnLike.setVisibility(View.INVISIBLE);
                holder.btnDisLike.setVisibility(View.VISIBLE);
            }
        else
            {
                holder.btnDisLike.setVisibility(View.INVISIBLE);
                holder.btnLike.setVisibility(View.VISIBLE);
            }
        holder.btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    xuLyYeuThich(music);
                    music.setLike(true);
                    holder.btnLike.setVisibility(View.INVISIBLE);
                    holder.btnDisLike.setVisibility(View.VISIBLE);
                }
            });
        holder.btnDisLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    xuLyKhongThich(music);
                    music.setLike(false);
                    holder.btnDisLike.setVisibility(View.INVISIBLE);
                    holder.btnLike.setVisibility(View.VISIBLE);
                }
            });

        return convertView;
    }

    private void xuLyKhongThich(BaiHat music) {
        ContentValues row = new ContentValues();
        row.put("YEUTHICH",0);
        MainActivity.database.update(
                "ArirangSongList",
                row,
                "MABH=?",
                new String[]{music.getMa()});
        if(lasteTabSelected == "t2")
            remove(music);
    }

    private void xuLyYeuThich(BaiHat music) {
        ContentValues row = new ContentValues();
        row.put("YEUTHICH",1);
        MainActivity.database.update(
                "ArirangSongList",
                row,
                "MABH=?",
                new String[]{music.getMa()});

    }

//    private class MusicFilter extends Filter{

//        @Override
//        protected FilterResults performFiltering(CharSequence charSequence) {
//            final FilterResults results = new FilterResults();
//            final List<BaiHat> nMusicList = new ArrayList<BaiHat>();
//            String search = charSequence.toString().toLowerCase();
//            if (charSequence == null || charSequence.length() == 0) {
//                // No filter implemented we return all the list
//                results.values = objects;
//                results.count = objects.size();
//            }
//            else {
//                // We perform filtering operation
//
//                for (final BaiHat baiHat : objects) {
//                    if (baiHat.getTen().toLowerCase()
//                            .startsWith(search))
//                        nMusicList.add(baiHat);
//                }
//
//                    results.values = nMusicList;
//                    results.count = nMusicList.size();
//
//            }
//            return results;
//        }
//
//        @SuppressWarnings("unchecked")
//        @Override
//        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//            // Now we have to inform the adapter about the new list filtered
////            if (filterResults.count == 0)
////                notifyDataSetInvalidated();
////            else {
//                dsFilter = (List<BaiHat>) filterResults.values;
//                MusicAdapter.this.notifyDataSetChanged();
//
//            //}
//        }
//    }

}

