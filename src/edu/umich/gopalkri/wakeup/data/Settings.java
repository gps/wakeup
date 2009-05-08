package edu.umich.gopalkri.wakeup.data;

/**
 * A Singleton class that initializes itself with the user's settings and provides an interface to
 * query and change all settings.
 *
 * @author Gopalkrishna Sharma (gopalkri@umich.edu / gopalkrishnaps@gmail.com)
 */
public class Settings
{
    private Settings mUniqueInstance = null;

    private Settings()
    {
    }

    public Settings getInstance()
    {
        if (mUniqueInstance == null)
        {
            mUniqueInstance = new Settings();
        }
        return mUniqueInstance;
    }
}
