# WordNet & NGram Explorer (Based on UCBerkeley's CS61B PROJ2)


## Features

### **Data Visualization**
- **Time Series Charts**: Interactive charts showing word frequency over time
- **Hyponym Analysis**: Find hierarchical word relationships and common hyponyms
- **Historical Trends**: Analyze word popularity across different time periods
- **Multi-word Queries**: Compare multiple words simultaneously



## Technology Stack

### **Backend**
- **Java 17+**: Core application logic
- **Spark Framework**: Lightweight web server
- **SLF4J**: Professional logging framework
- **XChart**: Chart generation and visualization

### **Frontend**
- **HTML5**: Semantic markup
- **CSS3**: Modern styling with Flexbox and Grid
- **JavaScript**: Interactive functionality
- **jQuery**: DOM manipulation and AJAX
- **Font Awesome**: Professional icons

### **Data Sources**
- **WordNet**: Lexical database for word relationships
- **Google NGram**: Historical word frequency data

## Prerequisites

- **Java 17** or higher
- **Git** for version control
- **Web browser** (Chrome, Firefox, Safari, Edge)

## Quick Start

### 1. **Clone the Repository**
```bash
git clone https://github.com/yourusername/wordnet-ngram-explorer.git
cd wordnet-ngram-explorer
```

### 2. **Download Dependencies**
Download the required JAR files and place them in `library/library-sp25/`:
- `algs4.jar` - Princeton Algorithms library
- `spark-core-2.9.4.jar` - Spark web framework
- `xchart-3.8.2.jar` - Chart generation
- `gson-2.10.jar` - JSON processing
- `slf4j-api-2.0.0.jar` - Logging framework
- `slf4j-simple-2.0.6.jar` - Logging implementation

### 3. **Download Data Files**
Place the following files in their respective directories:

**`data/ngrams/`:**
- `top_49887_words.csv` - Word frequency data
- `total_counts.csv` - Total word counts by year
- `very_short.csv` - Short word list (optional)

**`data/wordnet/`:**
- `synsets.txt` - WordNet synset definitions
- `hyponyms.txt` - WordNet hyponym relationships

### 4. **Compile the Application**
```bash
javac -cp "library/library-sp25/*:src" src/main/*.java src/ngrams/*.java src/plotting/*.java src/browser/*.java
```

### 5. **Run the Server**
```bash
java -cp "library/library-sp25/*:src" main.Main
```

### 6. **Access the Application**
Open your web browser and navigate to:
```
http://localhost:4567/ngordnet.html
```

## 📖 Usage Guide

### **Basic Operations**

1. **Enter Words**: Type one or more words separated by commas
2. **Set Time Range**: Choose start and end years (1400-2100)
3. **Limit Results**: Set maximum number of results (k) or leave empty for all
4. **Choose Analysis Type**:
   - **History Chart**: Visualize word frequency over time
   - **History Text**: Get frequency data as text
   - **Hyponyms**: Find hierarchical word relationships
   - **Hyponym History**: Chart hyponym frequencies

### **Keyboard Shortcuts**
- **Enter** (in words field): Trigger hyponyms search
- **Ctrl+Enter**: Quick hyponyms search
- **Escape**: Close loading overlay

## Project Structure

```
proj2b/
├── src/
│   ├── main/           # Core application logic
│   ├── browser/        # Web server and request handling
│   ├── ngrams/         # NGram data processing
│   ├── plotting/       # Chart generation
│   └── demo/           # Example demonstrations
├── static/             # Frontend assets
│   ├── ngordnet.html   # Main web interface
│   ├── ngordnet.css    # Professional styling
│   ├── ngordnet.js     # Interactive functionality
│   └── jquery.min.js   # jQuery library
├── data/               # Data files (not in repo)
│   ├── ngrams/         # NGram datasets
│   └── wordnet/        # WordNet datasets
├── library/            # Dependencies (not in repo)
│   └── library-sp25/   # JAR files
└── tests/              # Unit tests
```


## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- **CS61B Staff**: Original project framework
- **Princeton University**: Algorithms library
- **WordNet**: Lexical database
- **Google**: NGram dataset
- **Spark Framework**: Web server
- **XChart**: Chart generation
