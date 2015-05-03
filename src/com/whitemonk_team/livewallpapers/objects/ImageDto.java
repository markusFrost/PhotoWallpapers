package com.whitemonk_team.livewallpapers.objects;

import java.io.Serializable;
import java.util.HashMap;

import android.graphics.Rect;

public class ImageDto implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7908807293654335363L;
	public long _Id;
    public String Path;
    public boolean WasRotation;
    public HashMap<String, Rect> ImageMap;
}
