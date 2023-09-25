package io.github.jaredshapiro321.templatemod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ForgeItemTagsProvider;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import io.github.jaredshapiro321.templatemod.util.Color;

//import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TemplateMod.MODID)
public class TemplateMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "templatemod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "templatemod" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "templatemod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "templatemod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates a new Block with the id "templatemod:example_block", combining the namespace and path
    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
    // Creates a new BlockItem with the id "templatemod:example_block", combining the namespace and path
    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));

    // Creates a new food item with the id "templatemod:example_item", nutrition 1 and saturation 2
    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEat().nutrition(1).saturationMod(2f).build())));
    
    public static final RegistryObject<Item> TEST_ITEM = ITEMS.register("test_item", () -> new Item(new Item.Properties()));

    // Creates a creative tab with the id "templatemod:example_tab" for the example item, that is placed after the combat tab
    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());

    public TemplateMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(EXAMPLE_BLOCK_ITEM);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    public static void writeFile() {
        // Define the path to your file
        File file = new File("resourcepacks/file.txt");

        try {
            // Create the file if it doesn't exist
            if (!file.exists()) {
                file.createNewFile();
            }

            // Write data to the file
            FileWriter writer = new FileWriter(file);
            writer.write("Hello, Minecraft Forge Mod!");
            writer.close();

            // You can now perform any additional operations on the file.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean createDirectory(String directoryPath) {
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            return directory.mkdir(); // Create the directory
        } else {
            System.out.println("Directory already exists.");
            return false;
        }
    }
    
    public static void saveImageToFile(ResourceLocation imageLocation, ResourceManager resourceManager, String outputDirectory) {
        try {
            // Load the image as a NativeImage
            NativeImage image = NativeImage.read(resourceManager.getResource(imageLocation).get().open());

            LOGGER.info(outputDirectory);

            LOGGER.info(imageLocation.getPath());
            
            // Define the output file path
            File outputFile = new File(outputDirectory, imageLocation.getPath());

            // Ensure the output directory exists
            outputFile.getParentFile().mkdirs();

            // Save the image to the output file
            image.writeToFile(outputFile);

            // Don't forget to free the image data when you're done
            image.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static NativeImage compressImage(NativeImage originalImage, int targetColorCount) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        // Collect all pixel colors from the original image
        List<Integer> pixels = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int color = originalImage.getPixelRGBA(x, y);
                pixels.add(color);
            }
        }

        // Perform k-means clustering to find the target color palette
        List<Integer> palette = kMeansClustering(pixels, targetColorCount);

        // Create a new image with the compressed palette
        NativeImage compressedImage = new NativeImage(width, height, false);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int color = originalImage.getPixelRGBA(x, y);
                int closestColor = findClosestColor(color, palette);
                compressedImage.setPixelRGBA(x, y, closestColor);
            }
        }

        return compressedImage;
    }

    private static List<Integer> kMeansClustering(List<Integer> pixels, int k) {
        // Implement the k-means clustering algorithm to find k representative colors
        // You can use a library or implement it manually

        // For simplicity, let's generate k random colors as the initial centroids
        List<Integer> centroids = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < k; i++) {
            int randomColor = random.nextInt(16777215); // Generate a random RGB color
            centroids.add(randomColor);
        }

        // Implement k-means clustering here to update the centroids

        // Return the final palette of k colors
        return centroids;
    }

    private static int findClosestColor(int targetColor, List<Integer> palette) {
        // Find the color in the palette that is closest to the targetColor
        // You can implement a distance metric like Euclidean distance
        // and iterate through the palette to find the closest color

        int closestColor = palette.get(0); // Initialize with the first color
        // Implement the logic to find the closest color in the palette

        return closestColor;
    }
    
    
    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
            
            Color c = new Color("#00FF00");
            
            
            LOGGER.info(c.toString());
            LOGGER.info(c.toHex());
            
            /*
            try {
            	ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
				ResourceLocation resourceLocation = new ResourceLocation("templatemod", "textures/item/generated/test_item.png");
				
				InputStream stream = resourceManager.getResource(resourceLocation).get().open();
				NativeImage image = NativeImage.read(stream);
				
				//ResourceLocation outputLocation = new ResourceLocation("templatemod", "textures/item/edited_item.png");

				
				//Component description = ;
				
				String baseDirectory = "resourcepacks/templatemod_generated/assets/templatemod/";
				
				createDirectory(baseDirectory);
				//writeFile(baseDirectory + );
				
				saveImageToFile(resourceLocation, resourceManager, baseDirectory);
				
				Pack newPack = new Pack(15, "Description");
						
				McMeta mcMeta = new McMeta(newPack);

				
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				String json = gson.toJson(mcMeta);

		        // Specify the file path where you want to save the JSON data
		        String filePath = "resourcepacks/templatemod_generated/pack.mcmeta";

		        try (FileWriter writer = new FileWriter(filePath)) {
		            // Write the JSON data to the file
		            writer.write(json);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		        
		        HashSet<Item> ingots = new HashSet<Item>();
		        //LOGGER.info(ItemTags.LEAVES.toString());
		        LOGGER.info("Ingots");
		        for (Item item : ForgeRegistries.ITEMS.getValues()) {
		        	
		            if (item.toString() != null && item.toString().endsWith("_ingot")) {
		                // This item is an ingot
		                // You can do something with the ingot here

			        	LOGGER.info(item.toString());
			        	ingots.add(item);
		            }
		            
		        }
		        
		        HashMap<Item, Color> colorMap = new HashMap<Item, Color>();
		        
		        
		        for (Item item : ingots) {
		        	
		        	ResourceLocation newResourceLocation = new ResourceLocation("minecraft", "textures/item/" + item.toString() + ".png");
					
		        	LOGGER.info(newResourceLocation.getPath().toString());
		        	InputStream newStream = Minecraft.getInstance().getVanillaPackResources().getResource(PackType.CLIENT_RESOURCES, newResourceLocation).get();
					//InputStream newStream = resourceManager.getResource(newResourceLocation).get().open();
					NativeImage newImage = NativeImage.read(newStream);
				    

					Pallette<Color> colors = new Pallette<Color>();
					
					// Get the dimensions of the image
		            int width = newImage.getWidth();
		            int height = newImage.getHeight();

		            // Iterate through each pixel and print its color
		            for (int x = 0; x < width; x++) {
		                for (int y = 0; y < height; y++) {
		                    int color = newImage.getPixelRGBA(x, y);
		                    
		                    int alpha = (color >> 24) & 0xFF;
		                    
		                    if (alpha == 0) continue;
		                    
		                    int red = (color >> 16) & 0xFF;
		                    int green = (color >> 8) & 0xFF;
		                    int blue = color & 0xFF;
		                    
		                    Color newColor = new Color(red, green, blue, alpha);
		                    colors.add(newColor);
		                    //System.out.println("Pixel at (" + x + ", " + y + "):");
		                    //System.out.println("Alpha: " + alpha);
		                    //System.out.println("Red: " + red);
		                    //System.out.println("Green: " + green);
		                    //System.out.println("Blue: " + blue);
		                }
		                
		                
		                
		            }
					
					for (Color color : colors) {
						LOGGER.info(color.toString());
					}
					
					LOGGER.info(String.valueOf(colors.size()));

					try {
			            // Define the output file path
			            File outputFile = new File("resourcepacks/templatemod_generated/assets/templatemod/textures/item/" + item.toString() + ".png");

			            // Ensure the output directory exists
			            outputFile.getParentFile().mkdirs();

			            
			            NativeImage compressedImage = compressImage(newImage, 7);
			            // Save the image to the output file
			            compressedImage.writeToFile(outputFile);

						compressedImage.close();
			        } catch (IOException e) {
			            e.printStackTrace();
			        }

					newImage.close();
					
		        }
		      
		        
		        
		        

		        Minecraft.getInstance().getResourcePackRepository().reload();
		        Minecraft.getInstance().getResourcePackRepository().getAvailableIds().forEach(System.out::println);
		        ArrayList<String> collection = new ArrayList<String>();
		        collection.add("file/templatemod_generated");
		        
		        Minecraft.getInstance().getResourcePackRepository().setSelected(collection);
		        Minecraft.getInstance().getResourcePackRepository().reload();
		        */
				/*
				
				
				
				
				
				String id = "resourcePack.templatemod.name";
				
				String titleKey = "Template Mod Resources";
				Component title = Component.translatable(titleKey);
				
				Boolean required = true;
				
				TemplateModResourceSupplier resources = new TemplateModResourceSupplier();
				
				String descriptionKey = "Template Mod Resources";
				Component description = Component.translatable(descriptionKey);
				int format = 15;
				FeatureFlagSet requestedFeatures = FeatureFlagSet.of();
				Info info = new Info(description, format, requestedFeatures);
				
				PackType packType = PackType.CLIENT_RESOURCES;
				
				Position position = Position.TOP;
				
				Boolean fixedPosition = false;

				PackSource packSource = PackSource.DEFAULT;
				
				Pack pack = Pack.create(id, title, required, resources, info , packType, position, fixedPosition, packSource);
				
				
				
				
				Minecraft.getInstance().getResourcePackRepository().addPack(id);
				LOGGER.info(Minecraft.getInstance().getResourcePackRepository().getPack(id).toString());
				
				Minecraft.getInstance().getResourcePackRepository().getAvailablePacks().forEach(pack1 -> System.out.println(pack1.getTitle()));
				*/
				
				/*
				
				// Get the dimensions of the image
	            int width = image.getWidth();
	            int height = image.getHeight();

	            // Iterate through each pixel and print its color
	            for (int x = 0; x < width; x++) {
	                for (int y = 0; y < height; y++) {
	                    int color = image.getPixelRGBA(x, y);

	                    int alpha = (color >> 24) & 0xFF;
	                    int red = (color >> 16) & 0xFF;
	                    int green = (color >> 8) & 0xFF;
	                    int blue = color & 0xFF;

	                    System.out.println("Pixel at (" + x + ", " + y + "):");
	                    System.out.println("Alpha: " + alpha);
	                    System.out.println("Red: " + red);
	                    System.out.println("Green: " + green);
	                    System.out.println("Blue: " + blue);
	                }
	            }
				*/
			/*	
				//image.close();
            } catch (IOException e) {
				// TODO Auto-generated catch block
				LOGGER.info("OH GOD OH JEEZ");
				//e.printStackTrace();
			}
			*/
        }
    }
}
