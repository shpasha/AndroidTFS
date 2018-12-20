package xyz.shpasha.androidtfs.ui.test;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import xyz.shpasha.androidtfs.model.Problem;

import java.util.List;

public class TestViewPagerAdapter extends FragmentPagerAdapter {

    private List<Problem> problems;

    TestViewPagerAdapter(FragmentManager fragmentManager, List<Problem> problems) {
        super(fragmentManager);
        this.problems = problems;
    }

    @Override
    public int getCount() {
        return problems.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public TestPageFragment getItem(int position) {
        return TestPageFragment.newInstance(position, problems.get(position));
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }
}
