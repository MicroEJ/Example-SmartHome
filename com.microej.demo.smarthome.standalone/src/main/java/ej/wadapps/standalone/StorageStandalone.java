package ej.wadapps.standalone;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ej.wadapps.storage.Storage;

/**
 *
 */
public class StorageStandalone implements Storage {
	private static final String COMMON_DIRECTORY = "common"; //$NON-NLS-1$
	private File parent;

	@Override
	public void store(String id, InputStream stream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		StringBuilder string = new StringBuilder();
		while(stream.available()>0){
			string.append(reader.readLine());
		}
		store(id, string.toString());
	}

	@Override
	public InputStream load(String id) throws IOException {
		File file = new File(getParentDirectory(), id);
		if (file.exists()) {
			FileInputStream fin = new FileInputStream(file);
			return fin;
		}
		return null;

	}

	@Override
	public void remove(String id) throws IOException {
		File file = new File(getParentDirectory(), id);
		if (file.exists()) {
			file.delete();
		}
	}

	@Override
	public String[] getIds() throws IOException {
		File[] files = getParentDirectory().listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});

		String[] ids = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			ids[i] = files[i].getName();
		}

		return ids;
	}

	private synchronized File getParentDirectory() {
		if (parent == null) {
			File root = File.listRoots()[0];
			parent = new File(root, COMMON_DIRECTORY);
			if (!parent.exists()) {
				parent.mkdir();
			}
		}
		return parent;
	}

	public void store(String id, String value) {
		File file = new File(getParentDirectory(), id);
		if (!file.exists()) {
			try {
				file.createNewFile();
				FileOutputStream out = new FileOutputStream(file);
				out.write(value.getBytes());
				out.close();
			} catch (IOException e) {
			}
		}
	}

	public String read(String id) {

		StringBuilder builder = new StringBuilder();
		byte[] buffer = new byte[20];
		try {
			InputStream fin = load(id);
			if (fin != null) {
				int available = fin.available();
				while (available > 0) {
					int len = Math.min(available, buffer.length - 1);
					int read = fin.read(buffer, 0, len);
					if (read > 0) {
						builder.append(new String(buffer, 0, read));
					}
					available = fin.available();
				}
				fin.close();
				return builder.toString();
			}
		} catch (IOException e) {
		}
		return null;
	}
}