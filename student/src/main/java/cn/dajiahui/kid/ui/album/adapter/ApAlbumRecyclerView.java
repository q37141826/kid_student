//package cn.dajiahui.student.ui.album.adapter;
//
//import android.content.Context;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.GridView;
//import android.widget.TextView;
//
//import java.util.List;
//
//import cn.dajiahui.student.R;
//import cn.dajiahui.student.ui.album.AlbumActivity;
//import cn.dajiahui.student.ui.album.bean.BeAlbum;
//import cn.dajiahui.student.ui.album.bean.BeClassAlbum;
//import cn.dajiahui.student.util.DjhJumpUtil;
//
///**
// * Created by Mjj on 2016/5/31.
// */
//public class ApAlbumRecyclerView extends RecyclerView.Adapter<ApAlbumRecyclerView.MyViewHolder> {
//
//    private Context context;
//    private List<BeClassAlbum> list;
//
//    public ApAlbumRecyclerView(Context context, List<BeClassAlbum> list) {
//        this.context = context;
//        this.list = list;
//    }
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
//                context).inflate(R.layout.album_item_class, parent,
//                mfalse));
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
//        BeClassAlbum item = list.get(position);
//        holder.tvTitle.setText(item.getClassName());
//        ApAlbumTitle adapter = new ApAlbumTitle(context, item.getList(), position);
//        ((AlbumActivity) context).ablunList.add(adapter);
//        holder.gridView.setAdapter(adapter);
//        holder.gridView.setOnItemClickListener(onItemClickListener);
//    }
//
//    @Override
//    public int getItemCount() {
//        return list == null ? 0 : list.size();
//    }
//
//    class MyViewHolder extends RecyclerView.ViewHolder {
//        TextView tvTitle;
//        GridView gridView;
////        RecyclerView recyclerView;
//
//        public MyViewHolder(View view) {
//            super(view);
//            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
////            recyclerView = (RecyclerView) view.findViewById(R.id.gridview);
//            gridView = (GridView) view.findViewById(R.id.gridview);
//        }
//    }
//
//    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            BeAlbum album = list.get((int) id).getList().get(position);
//            DjhJumpUtil.getInstance().startPhotoActivity(context, album.getObjectId(), album.getName());
//        }
//    };
//}
