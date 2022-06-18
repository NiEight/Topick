package com.example.topic;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> implements Filterable {
    private ArrayList<ArticleContents> mlist, filteredList;
    private Context context;
    int bookMark_count = 0;

    public ArticleAdapter(ArrayList<ArticleContents> list, Context context) {
        mlist = list;
        this.filteredList = list;
        this.context = context;

        Log.v("Adapter생성자 호출","Adapter생성자");
    }

    public ArrayList<ArticleContents> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(ArrayList<ArticleContents> filteredList) {
        this.filteredList = filteredList;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            //검색 창에 단어 입력하면 요출 되는 메서드
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.v("performFiltering", "performFiltering :"+  constraint);
                String charString = constraint.toString();
                if(charString.isEmpty()) {
                    filteredList = mlist;
                } else {
                    ArrayList<ArticleContents> filteringList = new ArrayList<>();
                    for(ArticleContents name : mlist) {
                        if(name.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(name);
                        }
                    }
                    filteredList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            //performFiltering 메서드에서 필터링한 값을 매개변수로 받아 호출되는 메서드
            //마지막에 notifyDataSetChanged(); 호출하여 필터된 결과를 recyclerView를 출력한다.
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                Log.v("publishResults", "publishResults ");
                filteredList = (ArrayList<ArticleContents>)results.values;
                notifyDataSetChanged();
            }
        };
    }


    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.articleitem, parent, false);
        ArticleAdapter.ViewHolder vh = new ArticleAdapter.ViewHolder(view);

        return vh;
    }


    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(@NonNull ArticleAdapter.ViewHolder holder, int position) {

        ArticleContents item = filteredList.get(position);


        holder.title.setText(item.getTitle().replaceAll("&quot;","\""));
        holder.description.setText(item.getDescription().replaceAll("&quot;","\""));


        Log.v("onBindViewHolder 호출","position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시 "+"position: "+Integer.toString(position));

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        Log.v("getItemCount","전체 데이터 갯수 리턴: "+Integer.toString(filteredList.size()));
        return filteredList.size();
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title ;
        TextView description;

        ViewHolder(View itemView) {
            super(itemView) ;


            // 뷰 객체에 대한 참조. (hold strong reference)
            title = itemView.findViewById(R.id.titleText);
            description = itemView.findViewById(R.id.contentText);

            itemView.setOnCreateContextMenuListener((View.OnCreateContextMenuListener) context);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    Intent intent = new Intent(context ,WebViewActivity.class);
                    intent.putExtra("url", mlist.get(pos).getLink());
                    context.startActivity(intent);

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int pos = getAdapterPosition() ;

                    Toast.makeText(context, "북마크에 등록되었습니다.",Toast.LENGTH_LONG).show();
                    Log.d("str"," "+ title);
                    Intent intent = new Intent(context ,Bookmark.class);
                    intent.putExtra("title", mlist.get(pos).getTitle());
                    intent.putExtra("description", mlist.get(pos).getDescription());
                    intent.putExtra("link", mlist.get(pos).getLink());
                    intent.putExtra("a_bookMark_count", bookMark_count++);
                    context.startActivity(intent);
                    return true;
                }
            });

        }
    }


    //Item클릭 리스너 인터페이스
    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }


    private ArticleAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(ArticleAdapter.OnItemClickListener listener){
        this.mListener = listener;
    }

}
