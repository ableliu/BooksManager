package win.aspring.bookrxandroid.category;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import win.aspring.bookrxandroid.R;
import win.aspring.bookrxandroid.category.CategoryFragment.OnListFragmentInteractionListener;
import win.aspring.bookrxandroid.data.Category;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Category} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final List<Category> mValues;
    private final OnListFragmentInteractionListener mListener;

    private OnDeleteInteractionListener mInteractionListener;

    public void setOnDeleteInteractionListener(OnDeleteInteractionListener listener) {
        this.mInteractionListener = listener;
    }

    public interface OnDeleteInteractionListener {
        void onDeleteListener(Category item);
    }

    public CategoryAdapter(List<Category> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText("编号：" + mValues.get(position).getCategory_id());
        holder.mContentView.setText("名称：" + mValues.get(position).getCname());

        holder.mDelete.setOnClickListener(v -> {
            if (null != mInteractionListener) {
                mInteractionListener.onDeleteListener(holder.mItem);
            }
        });

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mIdView;
        final TextView mContentView;
        final ImageButton mDelete;
        Category mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.item_code);
            mContentView = view.findViewById(R.id.item_name);
            mDelete = view.findViewById(R.id.item_delete);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
