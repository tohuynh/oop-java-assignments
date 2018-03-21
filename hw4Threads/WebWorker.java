import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebWorker extends Thread {

	private String urlString;
	private int rowNum;
	private WebFrame wf;
	public WebWorker(String urlString, int rowNum, WebFrame wf) {
		this.urlString = urlString;
		this.rowNum = rowNum;
		this.wf = wf;
	}

	public void download() {
		InputStream input = null;
		StringBuilder contents = null;
		try {
			long startTime = System.currentTimeMillis();
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();

			// Set connect() to throw an IOException
			// if connection does not succeed in this many msecs.
			connection.setConnectTimeout(5000);

			connection.connect();
			input = connection.getInputStream();

			BufferedReader reader  = new BufferedReader(new InputStreamReader(input));

			char[] array = new char[1000];
			int len;
			contents = new StringBuilder(1000);
			while ((len = reader.read(array, 0, array.length)) > 0) {
				contents.append(array, 0, len);
				Thread.sleep(100);
			}

			// Successful download if we get here
			StringBuilder sb = new StringBuilder();
			SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
			String currentdate = dateFormat.format(new Date(System.currentTimeMillis()));
			sb.append(currentdate);
			sb.append(" ");
			sb.append(System.currentTimeMillis() - startTime);
			sb.append(" ms ");
			sb.append(contents.length());
			sb.append(" bytes");
			wf.updateTable(rowNum, sb.toString());
			

		}
		// Otherwise control jumps to a catch...
		catch(MalformedURLException ignored) {
			wf.updateTable(rowNum, "err");
		}
		catch(InterruptedException exception) {
			wf.updateTable(rowNum, "interrupted");
		}
		catch(IOException ignored) {
			wf.updateTable(rowNum, "err");
		}
		// "finally" clause, to close the input stream
		// in any case
		finally {
			try{
				if (input != null) input.close();
			}
			catch(IOException ignored) {}
		}

	}

	@Override
	public void run() {
		wf.signalWorkerStarted();
		wf.updateTable(rowNum, "fetching contents..");
		download();
		wf.signalWorkerEnded(true);
	}

}
