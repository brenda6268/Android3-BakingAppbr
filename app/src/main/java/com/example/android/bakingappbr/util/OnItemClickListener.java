package com.example.android.bakingappbr.util;

import android.view.View;



public interface OnItemClickListener<MODEL> {
    void onClick(MODEL model, View view);
}