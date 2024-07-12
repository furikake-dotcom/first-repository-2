package org.example.plugin.pluginsample;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.swing.plaf.synth.SynthLookAndFeel;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class Plugin_sample extends JavaPlugin implements Listener {

  @Override
  public void onEnable() {
    Bukkit.getPluginManager().registerEvents(this, this);
  }

  /**
   * プレイヤーがスニークを開始/終了する際に起動されるイベントハンドラ。
   *
   * @param e イベント
   */
  @EventHandler
  public void onPlayerToggleSneak(PlayerToggleSneakEvent e) {
    // イベント発生時のプレイヤーやワールドなどの情報を変数に持つ。
    Player player = e.getPlayer();
    World world = player.getWorld();

    List<Color> colorList = List.of(Color.ORANGE, Color.LIME, Color.PURPLE,
        Color.fromRGB(0, 104, 183));

    if (count % 2 == 0) {

      for (Color color : colorList) {
        //colorには一色ずつ色が入る

        // 花火オブジェクトをプレイヤーのロケーション地点に対して出現させる。
        Firework firework = world.spawn(player.getLocation(), Firework.class);

        // 花火オブジェクトが持つメタ情報を取得。
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        // メタ情報に対して設定を追加したり、値の上書きを行う。
        fireworkMeta.addEffect(
            FireworkEffect.builder()
                .withColor(color)
                .with(Type.BALL_LARGE)
                .withFade(Color.SILVER)
                .flicker(false)
                .build());
        fireworkMeta.setPower((int) Math.round(0.5 + 1));
        System.out.println(fireworkMeta);

        // 追加した情報で再設定する。
        firework.setFireworkMeta(fireworkMeta);
      }
      Path path = Path.of("firework.txt"); //C:\Users\minas\MineCraftにできる
      try {
        Files.writeString(path, "たーまやー",
            StandardOpenOption.APPEND); //StandardOpenOption.APPENDはfirework.txtがあることを前提に動かそうとしている
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
      try {
        player.sendMessage(Files.readString(path));
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
    count++;
  }

  int count = 1;

  //屈伸するたびにここに1が足される
  @EventHandler
  public void onPlayerToggleSneak(PlayerMoveEvent e) throws IOException {
  }

  @EventHandler
  public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
    //コマンドを実行したら ※クリエイティブモードはベッドに入れないので他のイベントにしました

    // イベント発生時のプレイヤーの情報を変数に持つ。
    Player player = e.getPlayer();

    ItemStack[] itemStacks = player.getInventory().getContents();
    Arrays.stream(itemStacks) //itemStacksは配列なので、Arrays.をつけてリスト型に変換する。
        .filter(
            item -> !Objects.isNull(item) && item.getMaxStackSize() == 64 && item.getAmount() < 64)
        .forEach(item -> item.setAmount(0));

    //インベントリがすべて埋まっておらず、空欄がある場合、itemの値としてnullが取得される
    //nullに対してgetMaxStackSize()を実行した際処理落ちするため、null回避を入れる。

        /*for (ItemStack item :itemStacks){
            if (!Objects.isNull(item) && item.getMaxStackSize() == 64  && item.getAmount() < 64){
                item.setAmount(64);
            }
        }*/

    // 更新したインベントリの情報を再設定する。
    player.getInventory().setContents(itemStacks);
  }
}