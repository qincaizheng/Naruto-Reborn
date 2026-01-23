package com.shadow_ninja.narutoreborn.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public String lacale;

    public ModLanguageProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
        this.lacale = locale;
    }

    @Override
    protected void addTranslations() {
        addlang("itemGroup.narutoreborn", "Naruto Reborn", "火影重生");

        // Messages / commands
        addlang("command.narutoreborn.ninjutsu.player_only", "Must be executed by a player", "必须由玩家执行");
        addlang("message.narutoreborn.ninjutsu.unknown", "Unknown ninjutsu: %s", "未知的忍术: %s");
        addlang("message.narutoreborn.ninjutsu.disabled", "This ninjutsu is disabled", "该忍术已禁用");
        addlang("message.narutoreborn.ninjutsu.locked", "This ninjutsu is locked", "该忍术未解锁");
        addlang("message.narutoreborn.ninjutsu.not_owned", "You have not learned this ninjutsu", "你尚未掌握该忍术");
        addlang("message.narutoreborn.ninjutsu.level_requirement", "Required level: %s", "等级不足，需要等级: %s");
        addlang("message.narutoreborn.ninjutsu.chakra_not_enough", "Not enough chakra: need %s", "查克拉不足，需要: %s");
        addlang("message.narutoreborn.ninjutsu.cooldown", "On cooldown: %ss remaining", "冷却中，还剩 %ss");
        addlang("message.narutoreborn.ninjutsu.used", "Used ninjutsu %s (power %s)", "已使用忍术 %s 威力 %s");
        addlang("message.narutoreborn.ninjutsu.learned", "Already learned", "已学习该忍术");
        addlang("message.narutoreborn.ninjutsu.learn", "Learned ninjutsu %s", "已学会忍术 %s");
        addlang("message.narutoreborn.ninjutsu.forget", "Forgot ninjutsu %s", "已遗忘忍术 %s");
        addlang("message.narutoreborn.ninjutsu.updated", "Updated ninjutsu %s", "已更新忍术 %s");
        addlang("message.narutoreborn.ninjutsu.quickbar.add", "Added to quickbar: %s", "已添加到快捷栏: %s");
        addlang("message.narutoreborn.ninjutsu.quickbar.remove", "Removed from quickbar: %s", "已从快捷栏移除: %s");
        addlang("message.narutoreborn.quickbar.empty", "Quickbar is empty", "快捷忍术列表为空");
        addlang("message.narutoreborn.permission_denied", "You do not have permission", "没有权限执行该命令");
        addlang("message.narutoreborn.data.level", "Set level to %s", "已将等级设置为 %s");
        addlang("message.narutoreborn.data.chakra", "Set chakra to %s", "已将查克拉设置为 %s");
        addlang("message.narutoreborn.data.maxchakra", "Set max chakra to %s", "已将最大查克拉设置为 %s");
        addlang("message.narutoreborn.data.experience", "Set experience to %s", "已将经验设置为 %s");
        addlang("message.narutoreborn.data.reset", "Reset ninja data", "已重置忍者数据");

        // Jutsu names/tooltips
        addlang("ninjutsu.narutoreborn.katon_fireball", "Katon: Great Fireball Technique", "火遁·豪火球之术");
        addlang("ninjutsu.narutoreborn.katon_fireball.tooltip", "Launch a fireball that deals AoE and ignites without block damage", "发射火球，造成范围伤害并点燃敌人，不破坏方块");
        addlang("ninjutsu.narutoreborn.unnamed", "Unnamed Jutsu", "未命名的忍术");
    }

    private void addlang(String key, String en, String cn) {
        switch (this.lacale) {
            case "en_us" -> this.add(key, en);
            case "zh_cn" -> this.add(key, cn);
        }
    }
}