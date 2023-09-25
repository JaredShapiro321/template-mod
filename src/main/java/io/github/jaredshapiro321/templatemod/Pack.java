package io.github.jaredshapiro321.templatemod;

public class Pack {
    private int pack_format;
    private String description;

    Pack(int packFormat, String description) {
    	this.pack_format = packFormat;
    	this.description = description;
    }

    @Override
    public String toString() {
        return "Pack{" +
                "description='" + description + '\'' +
                ", packFormat=" + pack_format +
                '}';
    }
}
