package byog.Core;

import byog.TileEngine.TERenderer;

public class Test {
    public static void main(String[] args) {
        DungeonGenerator dg = new DungeonGenerator();
        Stage stage = new Stage(81, 51);
        dg.initialize(stage, 30, 3);
        dg.generate(123456);

        DungeonGenerator dg2 = new DungeonGenerator();
        Stage stage2 = new Stage(81, 51);
        dg2.initialize(stage2, 30, 3);
        dg2.generate(123456);

        // 直接比较两个世界的tile数据
        boolean worldsAreIdentical = true;
        for (int x = 0; x < stage.width; x++) {
            for (int y = 0; y < stage.height; y++) {
                if (!dg.stage.world[x][y].equals(dg2.stage.world[x][y])) {
                    worldsAreIdentical = false;
                    System.out.println("差异点: (" + x + ", " + y + ")");
                    break;
                }
            }
            if (!worldsAreIdentical) {
                break;
            }
        }

        if (worldsAreIdentical) {
            System.out.println("成功！两个生成的世界完全相同。");
        } else {
            System.out.println("失败！两个世界存在差异。");
        }


        TERenderer ter = new TERenderer();
        ter.initialize(81, 51);
        ter.renderFrame(dg2.stage.world);
    }
}
