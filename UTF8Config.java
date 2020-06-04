package net.dxtrus.unicode.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.google.common.io.Files;

public class UTF8Config extends YamlConfiguration {

	public static UTF8Config loadConfiguration(File file) {
		UTF8Config utf8c = new UTF8Config();
		try {
			utf8c.load(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")));
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
			return null;
		}
		return utf8c;
	}

	public static UTF8Config loadConfiguration(Plugin plugin, String resource, File file) {

		InputStream defaultsStream = plugin.getResource(resource);

		if (!file.exists())
			try {
				Files.createParentDirs(file);
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		UTF8Config conf = new UTF8Config();
		conf.loadUtf8(file);
		
		if (defaultsStream!=null) {
			UTF8Config defaults = new UTF8Config();
			try {
				defaults.load(new InputStreamReader(defaultsStream, Charset.forName("UTF-8")));
				conf.setDefaults(defaults);
				conf.options().copyDefaults(true);
				
				conf.save(file);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
				plugin.getLogger().warning("Failed to load defaults for " + file.getAbsolutePath());
			}
			
		}
		
		
		return conf;

	}

	public void loadUtf8(File file) {
		try {
			this.load(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")));
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void load(File f) {
		loadUtf8(f);
	}
	
	@Override
    public void save(File file) throws IOException {
        Validate.notNull(file, "File cannot be null");
        Files.createParentDirs(file);
        String data = this.saveToString();
        Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8"));
        try {
            writer.write(data);
        } finally {
            writer.close();
        }
    }

}
