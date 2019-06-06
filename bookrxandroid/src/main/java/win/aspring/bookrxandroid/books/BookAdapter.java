package win.aspring.bookrxandroid.books;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import win.aspring.bookrxandroid.R;
import win.aspring.bookrxandroid.books.BookListFragment.OnListFragmentInteractionListener;
import win.aspring.bookrxandroid.data.Book;
import win.aspring.common.util.StringUtils;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Book} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private final List<Book> mValues;
    private final OnListFragmentInteractionListener mListener;

    private OnDeleteInteractionListener mInteractionListener;

    public void setOnDeleteInteractionListener(OnDeleteInteractionListener listener) {
        this.mInteractionListener = listener;
    }

    public interface OnDeleteInteractionListener {
        void onDeleteListener(Book item);
    }

    public BookAdapter(List<Book> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        String name = holder.mItem.getBook_name();
        if (StringUtils.isEmpty(name)) {
            name = "图书";
        }
        holder.mImg.setText(name.substring(0, 1));
        holder.mIdView.setText("编号：" + mValues.get(position).getBook_code());
        holder.mContentView.setText("名称：" + name);
        holder.mDelete.setOnClickListener(v -> {
            if (null != mInteractionListener) {
                mInteractionListener.onDeleteListener(holder.mItem);
            }
        });

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
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
        final TextView mImg;
        final TextView mIdView;
        final TextView mContentView;
        final ImageButton mDelete;
        Book mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mImg = view.findViewById(R.id.item_img);
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
