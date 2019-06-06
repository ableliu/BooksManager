package win.aspring.bookrxandroid.books;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import win.aspring.bookrxandroid.R;
import win.aspring.bookrxandroid.data.Category;

/**
 * 选择分类的适配器
 *
 * @author ASpring
 * @version 1.0
 * @since 2016-04-25 15:05
 */
public class SimpleAdapter extends BaseAdapter {
    private Context mContext;
    private List<Category> mList;

    public SimpleAdapter(Context context, List<Category> categories) {
        this.mContext = context;
        this.mList = categories;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_simple, null);

            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.item_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Category cate = (Category) getItem(position);
        holder.name.setText(cate.getCname());

        return convertView;
    }

    static class ViewHolder {
        TextView name;
    }
}
