package de.eisfeldj.augendiagnose.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import de.eisfeldj.augendiagnose.Application;
import de.eisfeldj.augendiagnose.R;
import de.eisfeldj.augendiagnose.util.ReleaseNotesUtil;

/**
 * A fragment to display an HTML page (used for help screens).
 */
public class DisplayHtmlFragment extends Fragment {
	/**
	 * The style tag to be inserted into the HTML.
	 */
	private static final String STYLE =
			"<style type=\"text/css\">body{color: #ffffff;} img {width: 24px; height: 24px; vertical-align: middle;} a {color: #7fffff;}</style>";

	/**
	 * The resource key for the resource to be displayed (for storage in the bundle).
	 */
	private static final String STRING_RESOURCE = "de.eisfeldj.augendiagnose.RESOURCE";

	/**
	 * The resource id of the HTML String to be displayed.
	 */
	private int resource;

	/**
	 * Initialize the listFoldersFragment with the resource.
	 *
	 * @param initialResource
	 *            The resource id of the HTML to be displayed.
	 */
	public final void setParameters(final int initialResource) {
		Bundle args = new Bundle();
		args.putInt(STRING_RESOURCE, initialResource);

		setArguments(args);
	}

	@Override
	public final void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		resource = getArguments().getInt(STRING_RESOURCE, -1);
	}

	@Override
	public final View onCreateView(final LayoutInflater inflater, final ViewGroup container,
			final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_display_html, container, false);
	}

	@Override
	public final void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		WebView webView = (WebView) getView().findViewById(R.id.webViewDisplayHtml);
		webView.setBackgroundColor(0x00000000);

		String html = getString(resource);
		if (resource == R.string.html_release_notes_base) {
			int indexBody = html.indexOf("</body>");
			String releaseNotes =
					ReleaseNotesUtil.getReleaseNotesHtml(getActivity(), false, 1, Application.getVersion());
			html = html.substring(0, indexBody) + releaseNotes + html.substring(indexBody);
		}
		else {
			html = getString(resource);
		}

		// remove link containing stylesheet
		int linkIndex1 = html.indexOf("<link");
		int linkIndex2 = html.indexOf(">", linkIndex1);
		int headIndex = html.indexOf("</head>", linkIndex2);
		if (linkIndex1 > 0 && linkIndex2 > 0 && headIndex > 0) {
			html = html.substring(0, linkIndex1) + html.substring(linkIndex2 + 1);
		}

		// add style
		int index = html.indexOf("</head>");
		html = html.substring(0, index) + STYLE + html.substring(index);

		webView.loadDataWithBaseURL("file:///android_res/drawable/", html, "text/html", "utf-8", "");
	}
}
