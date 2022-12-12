package it.iedx.login.domain;

import com.sun.source.doctree.SerialDataTree;

import java.io.Serializable;
import java.util.List;

public class MultiProfile implements Serializable {

    List<Profile> profiles;

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }
}
