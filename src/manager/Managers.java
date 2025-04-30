package manager;

import adapter.DurationAdapter;
import adapter.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.time.Duration;
import java.time.LocalDateTime;


public class Managers {
   // private static final InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    public static TaskManager getDefault(HistoryManager historyManager) {
        return new InMemoryTaskManager(historyManager);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }


    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        return gsonBuilder.create();
    }

}
