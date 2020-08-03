package com.confirmit.testsdkapp.layouts.defaultlayoutjava.items;

import java.util.List;

public interface DefaultLayoutSectionListener {
    void onReloadPage(List<IndexPath> add, List<IndexPath> refresh, List<IndexPath> remove);
}
