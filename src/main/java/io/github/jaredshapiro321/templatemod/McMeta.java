package io.github.jaredshapiro321.templatemod;

public class McMeta {
    private Pack pack;

    McMeta(Pack pack) {
    	this.pack = pack;
    }

    @Override
    public String toString() {
        return "MCMeta{" +
                "pack='" + pack + '\'' +
                '}';
    }
}
