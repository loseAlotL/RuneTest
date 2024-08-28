package org.randomlima.runetest.data;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.randomlima.runetest.RuneTest;
import org.randomlima.runetest.utilities.Colorize;
import org.randomlima.runetest.utilities.Msg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class StaffDataManager {
    private RuneTest plugin;
    private String fileName;
    private DataManager data;
    private DataManager lesserRingData;
    private ArrayList<String> staffNames = new ArrayList<>();
    private HashMap<String, List<String>> staffLore = new HashMap<>();
    private HashMap<String, String> staffDisplayNames = new HashMap<>();
    private HashMap<String, List<String>> staffAbilities = new HashMap<>();
    private HashMap<String, HashMap<Attribute, Integer>> staffAttributes = new HashMap<>();
    private HashMap<String, Material> staffMaterials = new HashMap<>();
    private HashMap<String, Integer> staffModelData = new HashMap<>();

    public StaffDataManager(RuneTest plugin, String fileName){
        this.fileName = fileName;
        this.plugin = plugin;
        this.data = new DataManager(plugin, this, fileName);
        update(data.getInternalConfig());
        load();
        updateLore();
    }
    public void load(){
        for(String ringName : getConfig().getConfigurationSection("rings").getKeys(false)){
            staffNames.add(ringName);
            for (String ringDetail : getConfig().getConfigurationSection("rings." + ringName).getKeys(false)) {
                try {
                    String path = "rings." + ringName + "." + ringDetail;
                    if (ringDetail.equals("display-name")) {
                        staffDisplayNames.put(ringName, getConfig().getString(path));
                    }
                    if (ringDetail.equals("abilities")) {
                        List<String> reverseList = getConfig().getStringList(path);
                        Collections.reverse(reverseList);
                        staffAbilities.put(ringName, reverseList);
                        for(String str : staffAbilities.get(ringName)){
                            if(!plugin.getAbilityDataManager().getAbilities().contains(str)) {
                                plugin.getServer().getConsoleSender().sendMessage(Colorize.format(Msg.prefix +
                                        "&4Error&c, the ability " + str + " in ring " + ringName + " does not exist."));
                                staffAbilities.get(ringName).remove(str);
                            }
                        }
                    }
                    if (ringDetail.equals("attributes")) {
                        for (String attributeSTR : getConfig().getConfigurationSection(path).getKeys(false)) {
                            try {
                                staffAttributes.put(ringName, NiceTools.castHashMap(Attribute.valueOf(attributeSTR), getConfig().getInt(path + "." + attributeSTR))); //this is broken.
                            } catch (Exception exception) {
                                plugin.getServer().getConsoleSender().sendMessage(Colorize.format(Msg.prefix + Msg.failedAttribute));
                            }
                        }
                    }
                    if (ringDetail.equals("item")) {
                        try {
                            staffMaterials.put(ringName, Material.valueOf(getConfig().getString(path)));
                        } catch (Exception e) {
                            plugin.getServer().getConsoleSender().sendMessage(Colorize.format(Msg.prefix + Msg.failedMaterial(ringDetail, ringName)));
                        }
                    }
                    if (ringDetail.equals("custom-model-data")) {
                        this.staffModelData.put(ringName, getConfig().getInt(path));
                    }
                } catch (Exception e) {
                    plugin.getServer().getConsoleSender().sendMessage(Colorize.format(Msg.failedRingLoad));
                    plugin.getServer().getConsoleSender().sendMessage(Colorize.format(Msg.usefulFailedRingLoad(ringName)));
                }
            }
        }
    }
    public void updateLore(){
        for(String ringName : getConfig().getConfigurationSection("rings").getKeys(false)) {
            String path = "rings."+ringName+".lore";
            List<String> loreEdit = getConfig().getStringList(path);
            for(int j = 0; j < loreEdit.size(); j++){
                if(loreEdit.get(j).equals(Msg.abilityDataConfigKey)){
                    loreEdit.remove(j);
                    for(String abilityName : staffAbilities.get(ringName)){
                        int jCount = 0;
                        try{
                            for(String loreElement : (ArrayList<String>)plugin.getAbilityDataManager().getAbilityData(abilityName, "description")){
                                loreEdit.add(j + jCount, loreElement);
                                jCount+=1;
                            }
                        }catch (Exception exception){
                            plugin.getServer().getConsoleSender().sendMessage(Colorize.format(Msg.usefulFailedRingLoad(ringName)));
                        }

                    }
                }
            }
            for(int j = 0; j < loreEdit.size(); j++){
                loreEdit.set(j, Colorize.format(loreEdit.get(j))); //colorize it all.
            }
            staffLore.put(ringName, loreEdit);
        }
    }
    public ArrayList<Attribute> getAttributes(String ringName){
        return (ArrayList<Attribute>) staffAttributes.get(ringName).keySet();
    }
    public List<String> getRingLore(String ringName){
        return staffLore.get(ringName);
    }
    public List<String> getAbilities(String ringName){
        return staffAbilities.get(ringName);
    }
    public HashMap<Attribute, Integer> getAttributeHash(String ringName){
        return staffAttributes.get(ringName);
    }
    public String getRingDisplayName(String ringName){
        return staffDisplayNames.get(ringName);
    }
    public ArrayList<String> getRingNames(){
        return staffNames;
    }
    public int getModelData(String ringName){
        return staffModelData.get(ringName);
    }
    public Material getRingMaterial(String ringName){
        return staffMaterials.get(ringName);
    }

    @Override
    public YamlConfiguration getConfig() {
        return data.getConfig();
    }

    @Override
    public void set(String path, Object object) {
        data.set(path, object);
    }

    @Override
    public void remove(String path) {
        data.remove(path);
    }

    @Override
    public void update(YamlConfiguration internalYamlConfig) {
        set("version", internalYamlConfig.get("version"));
        //then needs to loop through to see if it does not have a new added item.
    }
}
