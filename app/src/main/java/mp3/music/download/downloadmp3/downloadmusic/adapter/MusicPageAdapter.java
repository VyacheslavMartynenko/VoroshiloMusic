package mp3.music.download.downloadmp3.downloadmusic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import mp3.music.download.downloadmp3.downloadmusic.fragment.enter.EnterFragment;

public class MusicPageAdapter extends SmartFragmentStatePagerAdapter {
    private final static int NUM_ITEMS = 3;

    public MusicPageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return EnterFragment.newInstance(0);
            case 1:
                return EnterFragment.newInstance(1);
            case 2:
                return EnterFragment.newInstance(2);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
