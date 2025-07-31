#!/bin/bash

# GitHub Repository Setup Script
# This script helps you connect your local repository to GitHub

echo "🚀 GitHub Repository Setup"
echo "=========================="

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

# Check if git is initialized
if [ ! -d ".git" ]; then
    print_warning "Git repository not found. Please run 'git init' first."
    exit 1
fi

# Check if we have commits
if ! git rev-parse HEAD >/dev/null 2>&1; then
    print_warning "No commits found. Please make an initial commit first."
    exit 1
fi

print_info "Your local repository is ready!"
print_info "Follow these steps to upload to GitHub:"
echo ""

echo "1. Go to https://github.com and create a new repository"
echo "   - Name: wordnet-ngram-explorer (or your preferred name)"
echo "   - Description: A modern Java web application for exploring WordNet hyponyms and Google NGram data"
echo "   - DO NOT initialize with README, .gitignore, or license"
echo ""

echo "2. After creating the repository, run these commands:"
echo ""

# Get current directory name as suggested repo name
REPO_NAME=$(basename "$PWD")
echo "   git remote add origin https://github.com/YOUR_USERNAME/$REPO_NAME.git"
echo "   git branch -M main"
echo "   git push -u origin main"
echo ""

print_info "Replace 'YOUR_USERNAME' with your actual GitHub username"
print_info "Replace '$REPO_NAME' with your actual repository name if different"
echo ""

print_success "Your project is ready for GitHub! 🎉"
print_info "See github-setup.md for detailed instructions" 