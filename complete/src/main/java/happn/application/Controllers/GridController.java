package happn.application.Controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import happn.application.Models.Grid;
import happn.application.Services.GridService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GridController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private GridService gridService;

    @RequestMapping("/grid")
    public Map grid(@RequestParam(value="name", defaultValue="World") String name) {
        gridService.readDataFile();
        gridService.printGridData();
        gridService.getMostDenseZones(2);
        return gridService.getGrid().getResult();
    }

    @RequestMapping("/somme")
    public Map somme(@RequestParam(value="integer1") Integer integer1,
                          @RequestParam(value="integer2") Integer integer2) {
        Map<String,Integer> result = new HashMap<>();
        result.put("result", gridService.somme(integer1, integer2));
        return result;
    }
}
