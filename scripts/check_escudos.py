import csv
import os
import unicodedata
import re

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DATA = os.path.join(ROOT, 'DATA', 'equips.csv')
ESC = os.path.join(ROOT, 'LOGO', 'ESCUDOS')

def normalize(s):
    if not s: return ''
    s = unicodedata.normalize('NFD', s)
    s = ''.join(ch for ch in s if not unicodedata.combining(ch))
    s = s.lower()
    s = re.sub(r'[^a-z0-9]+', '-', s)
    s = re.sub(r'(^-+|-+$)', '', s)
    return s

# load escudos filenames per folder
folders = [d for d in os.listdir(ESC) if os.path.isdir(os.path.join(ESC,d))]
files_by_folder = {}
all_files = []
for f in folders:
    path = os.path.join(ESC, f)
    names = []
    for root,_,files in os.walk(path):
        for fn in files:
            names.append(fn.lower())
            all_files.append(os.path.join(root,fn))
    files_by_folder[f] = names

missing = {}

with open(DATA, newline='', encoding='utf-8') as csvfile:
    reader = csv.DictReader(csvfile, delimiter=';')
    for row in reader:
        league = row['Lliga'].strip()
        team = row['Equip'].strip()
        norm_team = normalize(team)
        # map league to folder by matching normalized names
        norm_league = normalize(league)
        matched_folder = None
        for folder in folders:
            if norm_league.replace('-','') in folder.replace('-','') or folder.replace('-','') in norm_league.replace('-',''):
                matched_folder = folder
                break
        # generate candidates
        candidates = set()
        candidates.add(norm_team)
        candidates.add(re.sub(r'^\d+-','',norm_team))
        parts = norm_team.split('-')
        if parts:
            candidates.add(parts[-1])
        candidates.add(re.sub(r'^(fsv|vfl|vfb|fc)-','',norm_team))
        candidates.add(re.sub(r'-(cf|fc|c-f|c-f-)$','',norm_team))
        overrides = {
            '1899-hoffenheim':'hoffenheim',
            'vfl-wolfsburg':'wolfsburg',
            'fsv-mainz-05':'mainz-05',
            'vfl-bochum':'bochum',
            'sheffield-utd':'sheffield-united',
            'stade-brestois-29':'brest',
            'bayern-munich':'bayern-munchen',
            'sv-darmstadt-98':'darmstadt',
            'psv-eindhoven':'psv',
            'celta-vigo':'celta'
        }
        if norm_team in overrides:
            candidates.add(overrides[norm_team])
        found = False
        # check league folder first
        search_folders = []
        if matched_folder:
            search_folders.append(matched_folder)
        search_folders.extend([f for f in folders if f not in search_folders])
        for sf in search_folders:
            for cand in candidates:
                for ext in ['.png','.jpg','.webp']:
                    if (cand + ext) in files_by_folder[sf]:
                        found = True
                        break
                if found: break
            if found: break
        # fuzzy check in all files
        if not found:
            for fn in all_files:
                ln = os.path.basename(fn).lower()
                for cand in candidates:
                    if cand and cand in ln:
                        found = True
                        break
                if found: break
        if not found:
            # debug output
            print('DEBUG: no match for', team, 'norm=', norm_team, 'candidates=', sorted(candidates), 'matched_folder=', matched_folder)
            missing.setdefault(league, []).append(team)

# print missing
for liga, teams in missing.items():
    print(liga + ':')
    for t in teams:
        print('  -', t)
    print()
print('Summary: leagues with missing counts:')
for liga in sorted(missing.keys()):
    print(f"{liga}: {len(missing[liga])}")
