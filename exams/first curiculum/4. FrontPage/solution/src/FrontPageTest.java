import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalField;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for(Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch(CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}

// Vasiot kod ovde

class CategoryNotFoundException extends Exception {
    private String categoryName;

    public CategoryNotFoundException(String categoryName){
        this.categoryName = categoryName;
    }

    @Override
    public String getMessage() {
        return String.format("Category %s was not found", categoryName);
    }
}

class Category {
    private String categoryName;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(this == obj) return true;
        if(getClass() != obj.getClass()) return false;

        Category c = (Category)obj;
        if(categoryName.equals(c.getCategoryName()))
            return true;

        return false;
    }
}

abstract class NewsItem {
    protected String title;
    protected Date date;
    protected Category category;

    protected NewsItem(String title, Date date, Category category) {
        this.title = title;
        //this.date = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        this.date = date;
        this.category = category;
    }

    protected int getMinutesPostedAgo() {
        long difference = new Date().getTime() - date.getTime();
        long minutes = (difference / 1000) / 60;
        return (int)minutes;
    }

    public abstract String getTeaser();
}

class TextNewsItem extends NewsItem {
    private String content;

    protected TextNewsItem(String title, Date date, Category category, String content) {
        super(title, date, category);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String getTeaser() {
        String previewContent = content.length() > 80
                ? content.substring(0, 80)
                : content;

        return String.format("%s\n%d\n%s\n", title, getMinutesPostedAgo(), previewContent);
    }
}

class MediaNewsItem extends NewsItem {
    private String url;
    private int views;

    protected MediaNewsItem(String title, Date date, Category category, String url, int views) {
        super(title, date, category);
        this.url = url;
        this.views = views;
    }

    @Override
    public String getTeaser() {
        return String.format("%s\n%d\n%s\n%d\n", title, getMinutesPostedAgo(), url, views);
    }
}

class FrontPage {
    private List<NewsItem> newsItems;
    private Category[] categories;

    public FrontPage(Category[] categories) {
        this.newsItems = new ArrayList<>();
        this.categories = Arrays.copyOf(categories, categories.length);
    }

    public void addNewsItem(NewsItem newsItem) {
        newsItems.add(newsItem);
    }

    public List<NewsItem> listByCategory(Category category) {
        return newsItems.stream().filter(x -> x.category.equals(category)).collect(Collectors.toList());
    }

    public List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException {
        Category c = Arrays.stream(categories).filter(x -> x.getCategoryName().equals(category)).findFirst().orElse(null);
        if(c == null)
            throw new CategoryNotFoundException(category);

        return listByCategory(c);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(NewsItem newsItem : newsItems) {
            sb.append(newsItem.getTeaser());
        }
        return sb.toString();
    }
}