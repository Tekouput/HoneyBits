package com.teko.honeybits.honeybits.support;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teko.honeybits.honeybits.R;
import com.teko.honeybits.honeybits.models.Category;
import com.tokenautocomplete.TokenCompleteTextView;

public class CategoryCompletionView extends TokenCompleteTextView<Category> {

    public CategoryCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(Category category) {

        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TextView view = (TextView) l.inflate(R.layout.category_token, (ViewGroup) getParent(), false);
        view.setText(category.getName());

        return view;
    }

    @Override
    protected Category defaultObject(String completionText) {
        //Stupid simple example of guessing if we have an email or not
        int index = completionText.indexOf('@');
        if (index == -1) {
            return new Category("-1", completionText.replace(" ", ""), "New category");
        } else {
            return new Category("-2", completionText.substring(0, index), completionText);
        }
    }
}