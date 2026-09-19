/**
 * BM Muma Player - Sonic Tangerine Audio Engine & Interface Logic
 */

// Track & Media Catalog
const TRACKS = [
  {
    id: 1,
    title: "Starboy",
    artist: "The Weeknd ft. Daft Punk",
    album: "Starboy",
    duration: 230,
    cover: "https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17?auto=format&fit=crop&w=600&q=80",
    isFavorite: true,
    lyrics: [
      { time: 0, text: "[Instrumental Synth Intro]" },
      { time: 10, text: "I'm tryna put you in the worst mood, ah" },
      { time: 14, text: "P1 cleaner than your church shoes, ah" },
      { time: 18, text: "Milli point two just to hurt you, ah" },
      { time: 22, text: "All red Lamb' just to tease you, ah" },
      { time: 26, text: "None of these toys on lease too, ah" },
      { time: 30, text: "Made your whole year in a week too, yah" },
      { time: 34, text: "Main bitch out your league too, ah" },
      { time: 38, text: "Side bitch out of your league too, ah" },
      { time: 42, text: "Look what you've done" },
      { time: 46, text: "I'm a motherfuckin' starboy" },
      { time: 51, text: "Look what you've done" },
      { time: 55, text: "I'm a motherfuckin' starboy" },
      { time: 60, text: "Every day a nigga try to test me, ah" },
      { time: 64, text: "Every day a nigga try to end me, ah" }
    ]
  },
  {
    id: 2,
    title: "Blinding Lights",
    artist: "The Weeknd",
    album: "After Hours",
    duration: 200,
    cover: "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?auto=format&fit=crop&w=600&q=80",
    isFavorite: true,
    lyrics: [
      { time: 0, text: "[80s Synth Wave Opening]" },
      { time: 8, text: "Yeah..." },
      { time: 14, text: "I've been tryna call" },
      { time: 18, text: "I've been on my own for long enough" },
      { time: 24, text: "Maybe you can show me how to love, maybe" },
      { time: 31, text: "I'm going through withdrawals" },
      { time: 36, text: "You don't even have to do too much" },
      { time: 40, text: "You can turn me on with just a touch, baby" },
      { time: 46, text: "I look around and Sin City's cold and empty" },
      { time: 51, text: "No one's around to judge me" },
      { time: 55, text: "I can't see clearly when you're gone" },
      { time: 60, text: "I said, ooh, I'm blinded by the lights!" }
    ]
  },
  {
    id: 3,
    title: "Positions",
    artist: "Ariana Grande",
    album: "Positions",
    duration: 172,
    cover: "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?auto=format&fit=crop&w=600&q=80",
    isFavorite: false,
    lyrics: [
      { time: 0, text: "Heaven sent you to me" },
      { time: 6, text: "I'm just hopin' I don't repeat history" },
      { time: 13, text: "Boy, I'm tryna meet your mama on a Sunday" },
      { time: 18, text: "Then make a lotta love on a Monday" },
      { time: 23, text: "Never need no, no one else, babe" },
      { time: 28, text: "Cause I'll be switchin' them positions for you" },
      { time: 34, text: "Cookin' in the kitchen and I'm in the bedroom" },
      { time: 40, text: "I'm in the Olympics, way I'm jumpin' through hoops" }
    ]
  },
  {
    id: 4,
    title: "Midnight City",
    artist: "M83",
    album: "Hurry Up, We're Dreaming",
    duration: 243,
    cover: "https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?auto=format&fit=crop&w=600&q=80",
    isFavorite: false,
    lyrics: [
      { time: 0, text: "[Neon Vocal Echo]" },
      { time: 15, text: "Waiting in a car" },
      { time: 22, text: "Waiting for a ride in the dark" },
      { time: 30, text: "The night city grows" },
      { time: 37, text: "Look and see her eyes, they glow" },
      { time: 45, text: "Waiting in a car" },
      { time: 52, text: "Waiting for a ride in the dark" },
      { time: 60, text: "[Legendary Saxophone Solo Breakdown]" }
    ]
  },
  {
    id: 5,
    title: "Save Your Tears",
    artist: "The Weeknd",
    album: "After Hours",
    duration: 215,
    cover: "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?auto=format&fit=crop&w=600&q=80",
    isFavorite: true,
    lyrics: [
      { time: 0, text: "Ooh, na-na, yeah" },
      { time: 8, text: "I saw you dancing in a crowded room" },
      { time: 14, text: "You look so happy when I'm not with you" },
      { time: 20, text: "But then you saw me, caught you by surprise" },
      { time: 26, text: "A single teardrop falling from your eye" },
      { time: 32, text: "I don't know why I run away" },
      { time: 38, text: "I'll make you cry when I run away" }
    ]
  },
  {
    id: 6,
    title: "7 Rings",
    artist: "Ariana Grande",
    album: "Thank U, Next",
    duration: 178,
    cover: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=600&q=80",
    isFavorite: true,
    lyrics: [
      { time: 0, text: "Breakfast at Tiffany's and bottles of bubbles" },
      { time: 5, text: "Girls with tattoos who like getting in trouble" },
      { time: 10, text: "Lashes and diamonds, ATM machines" },
      { time: 14, text: "Buy myself all of my favorite things" },
      { time: 18, text: "I want it, I got it, I want it, I got it" },
      { time: 24, text: "You like my hair? Gee, thanks, just bought it" }
    ]
  }
];

const ARTISTS = [
  {
    id: "the-weeknd",
    name: "The Weeknd",
    followers: "108.4M Monthly Listeners",
    songsCount: 52,
    avatar: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80"
  },
  {
    id: "ariana-grande",
    name: "Ariana Grande",
    followers: "84.2M Monthly Listeners",
    songsCount: 48,
    avatar: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80"
  },
  {
    id: "m83",
    name: "M83",
    followers: "14.6M Monthly Listeners",
    songsCount: 26,
    avatar: "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?auto=format&fit=crop&w=400&q=80"
  },
  {
    id: "daft-punk",
    name: "Daft Punk",
    followers: "32.1M Monthly Listeners",
    songsCount: 38,
    avatar: "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?auto=format&fit=crop&w=400&q=80"
  }
];

const ALBUMS = [
  {
    id: "starboy",
    title: "Starboy",
    artist: "The Weeknd",
    year: "2016",
    tracks: 18,
    cover: "https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17?auto=format&fit=crop&w=600&q=80"
  },
  {
    id: "after-hours",
    title: "After Hours",
    artist: "The Weeknd",
    year: "2020",
    tracks: 14,
    cover: "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?auto=format&fit=crop&w=600&q=80"
  },
  {
    id: "positions-album",
    title: "Positions",
    artist: "Ariana Grande",
    year: "2020",
    tracks: 14,
    cover: "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?auto=format&fit=crop&w=600&q=80"
  },
  {
    id: "hurry-up",
    title: "Hurry Up, We're Dreaming",
    artist: "M83",
    year: "2011",
    tracks: 22,
    cover: "https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?auto=format&fit=crop&w=600&q=80"
  }
];

const FOLDERS = [
  {
    id: "top-100",
    name: "Top 100 Billboard",
    path: "/storage/emulated/0/Music/Billboard",
    count: 120
  },
  {
    id: "synthwave",
    name: "Synthwave & Retrowave",
    path: "/storage/emulated/0/Music/Retro",
    count: 64
  },
  {
    id: "favorites",
    name: "Hi-Res Flac Collection",
    path: "/storage/emulated/0/Music/FLAC",
    count: 88
  },
  {
    id: "voice-memos",
    name: "Voice Recordings",
    path: "/storage/emulated/0/Recordings",
    count: 42
  }
];

const WHATSAPP_VOICES = [
  {
    id: "wa-1",
    title: "Voice note from Mom",
    sender: "Mom ❤️",
    duration: 38,
    date: "Today, 4:15 PM",
    format: "OPUS • 24kbps",
    type: "voice_note"
  },
  {
    id: "wa-2",
    title: "PTT-20260918-WA0014.opus",
    sender: "Alex Dev Lead",
    duration: 72,
    date: "Today, 11:30 AM",
    format: "OPUS • 24kbps",
    type: "voice_note"
  },
  {
    id: "wa-3",
    title: "Voice note from Sarah",
    sender: "Sarah Jenkins",
    duration: 19,
    date: "Yesterday, 9:42 PM",
    format: "OPUS • 24kbps",
    type: "voice_note"
  },
  {
    id: "wa-4",
    title: "Project Audio Clip - Mix Idea",
    sender: "BM Studio Group",
    duration: 145,
    date: "Sep 16, 2:10 PM",
    format: "AAC • 64kbps",
    type: "audio_clip"
  },
  {
    id: "wa-5",
    title: "PTT-20260915-WA0088.opus",
    sender: "David Audio Eng",
    duration: 54,
    date: "Sep 15, 6:05 PM",
    format: "OPUS • 24kbps",
    type: "voice_note"
  }
];

const RECORDINGS = [
  {
    id: "rec-1",
    title: "Meeting Memo - Architecture Sync",
    location: "Studio Mic",
    duration: 340,
    date: "Today, 2:00 PM",
    format: "M4A • 128kbps",
    size: "5.4 MB"
  },
  {
    id: "rec-2",
    title: "Acoustic Guitar Hook Idea #3",
    location: "Voice Recorder",
    duration: 88,
    date: "Yesterday, 8:15 PM",
    format: "WAV • 44.1kHz",
    size: "14.2 MB"
  },
  {
    id: "rec-3",
    title: "Call Recording - Client Discussion",
    location: "Call Recorder",
    duration: 492,
    date: "Sep 17, 10:20 AM",
    format: "M4A • 96kbps",
    size: "6.8 MB"
  },
  {
    id: "rec-4",
    title: "Vocal Melody Hook (Tangerine)",
    location: "Voice Recorder",
    duration: 42,
    date: "Sep 14, 11:45 PM",
    format: "M4A • 128kbps",
    size: "820 KB"
  }
];

// App State
let currentTrackIndex = 0;
let isPlaying = false;
let currentTime = 0;
let isShuffle = false;
let isRepeat = false;
let historyQueue = [];
let audioContext = null;
let synthTimer = null;
let bgVisibilityTimer = null;
let currentSelectedMenuTrack = 0;
let currentClassification = 'music'; // 'music' | 'whatsapp' | 'recordings'
let currentMusicSubFilter = 'all'; // 'all' | 'favorites' | 'recent'
let musicSortMode = 'recent'; // 'recent' | 'title' | 'artist'
let currentVoiceTrack = null;
let voiceSortAsc = false;
let screenHistory = ['paneHome'];

// Format duration mm:ss
function formatTime(seconds) {
  const mins = Math.floor(seconds / 60);
  const secs = Math.floor(seconds % 60);
  return `${mins}:${secs < 10 ? '0' : ''}${secs}`;
}

// ==========================================================================
// Web Audio Synthesizer Engine (Allows full offline music simulation)
// ==========================================================================
function initAudioEngine() {
  if (!audioContext) {
    const AudioCtx = window.AudioContext || window.webkitAudioContext;
    if (AudioCtx) {
      audioContext = new AudioCtx();
    }
  }
}

function playSynthBeep(freq = 440, duration = 0.15) {
  try {
    initAudioEngine();
    if (!audioContext || audioContext.state === 'suspended') {
      audioContext.resume();
    }
    const osc = audioContext.createOscillator();
    const gain = audioContext.createGain();
    osc.type = 'triangle';
    osc.frequency.setValueAtTime(freq, audioContext.currentTime);
    gain.gain.setValueAtTime(0.12, audioContext.currentTime);
    gain.gain.exponentialRampToValueAtTime(0.001, audioContext.currentTime + duration);
    osc.connect(gain);
    gain.connect(audioContext.destination);
    osc.onended = () => {
      try {
        osc.disconnect();
        gain.disconnect();
      } catch (_) {}
    };
    osc.start();
    osc.stop(audioContext.currentTime + duration);
  } catch (e) {
    // Silent fail if browser audio blocked
  }
}

// ==========================================================================
// Navigation & Screen Router
// ==========================================================================
function navigateTo(paneId) {
  if (screenHistory[screenHistory.length - 1] !== paneId) {
    screenHistory.push(paneId);
    if (screenHistory.length > 25) screenHistory.shift();
  }

  const panes = document.querySelectorAll('.screen-pane');
  panes.forEach(p => p.classList.remove('active'));
  
  const target = document.getElementById(paneId);
  if (target) {
    target.classList.add('active');
    target.scrollTop = 0;
  }
  const phone = document.querySelector('.device-phone');
  if (phone) phone.scrollTop = 0;

  // Update bottom nav highlighting
  const navMap = {
    'paneHome': 'navHome',
    'paneSongs': 'navSongs',
    'paneArtists': 'navArtists',
    'paneAlbums': 'navAlbums',
    'paneFolders': 'navFolders'
  };

  document.querySelectorAll('.nav-item').forEach(btn => btn.classList.remove('active'));
  if (navMap[paneId]) {
    const activeNav = document.getElementById(navMap[paneId]);
    if (activeNav) activeNav.classList.add('active');
  }

  // Auto-fill detail views when navigating
  if (paneId === 'paneRecentlyPlayed') {
    renderRecentlyPlayed();
  }
  if (paneId === 'paneFavorites') {
    renderFavoritesScreen();
  }
}

function goBack() {
  if (screenHistory.length > 1) {
    screenHistory.pop();
    const prevPane = screenHistory.pop() || 'paneHome';
    navigateTo(prevPane);
  } else {
    navigateTo('paneHome');
  }
}

function goBackFromSongs() {
  if (currentClassification !== 'music') {
    // If viewing WhatsApp or Recordings, go back to Pure Music
    switchSongClassification('music');
  } else if (currentMusicSubFilter !== 'all') {
    // If viewing Favorites or Recent sub-filter, go back to All Music
    setMusicSubFilter('all');
  } else {
    // Already in Pure Music: navigate to Home
    navigateTo('paneHome');
  }
}

// ==========================================================================
// Playback State Management
// ==========================================================================
function loadTrack(index) {
  currentVoiceTrack = null;
  currentTrackIndex = (index + TRACKS.length) % TRACKS.length;
  const track = TRACKS[currentTrackIndex];
  currentTime = 0;

  // Add to history
  if (!historyQueue.includes(track.id)) {
    historyQueue.unshift(track.id);
  }

  // Update docked mini player
  document.getElementById('dockedThumb').src = track.cover;
  document.getElementById('dockedTitle').textContent = track.title;
  document.getElementById('dockedArtist').textContent = track.artist;

  // Update full player
  document.getElementById('fullPlayerCover').src = track.cover;
  document.getElementById('fullPlayerTitle').textContent = track.title;
  document.getElementById('fullPlayerArtist').textContent = track.artist;
  document.getElementById('timeRemaining').textContent = formatTime(track.duration);
  document.getElementById('timeCurrent').textContent = "0:00";
  updateScrubber(0);

  // Update lyrics pane header
  document.getElementById('lyricsThumb').src = track.cover;
  document.getElementById('lyricsTrackTitle').textContent = track.title;
  document.getElementById('lyricsTrackArtist').textContent = track.artist;
  renderLyrics(track);

  // Pre-populate track context menu so it never has empty artwork or dummy text
  const sheetThumb = document.getElementById('sheetThumb');
  if (sheetThumb) sheetThumb.src = track.cover;
  const sheetTitle = document.getElementById('sheetTitle');
  if (sheetTitle) sheetTitle.textContent = track.title;
  const sheetArtist = document.getElementById('sheetArtist');
  if (sheetArtist) sheetArtist.textContent = track.artist;

  // Update favorite icon in player
  const favBtn = document.getElementById('fullPlayerFavBtn');
  favBtn.style.color = track.isFavorite ? 'var(--favorite)' : 'var(--slate-muted)';

  // Refresh active highlighting across lists
  highlightActiveTrack();
}

function togglePlayPause() {
  initAudioEngine();
  isPlaying = !isPlaying;
  updatePlayPauseUI();

  if (isPlaying) {
    if (audioContext && audioContext.state === 'suspended') {
      audioContext.resume();
    }
    playSynthBeep(520, 0.2);
    startPlaybackTimer();
  } else {
    stopPlaybackTimer();
    if (audioContext && audioContext.state === 'running') {
      audioContext.suspend();
    }
  }

  if (currentVoiceTrack) {
    renderAllSongs();
  }
}

function updatePlayPauseUI() {
  const playSvg = '<svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor"><path d="M8 5v14l11-7z"/></svg>';
  const pauseSvg = '<svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor"><path d="M6 19h4V5H6v14zm8-14v14h4V5h-4z"/></svg>';

  document.getElementById('dockedPlayBtn').innerHTML = isPlaying ? pauseSvg : playSvg;
  document.getElementById('btnMainPlay').innerHTML = isPlaying ? pauseSvg : playSvg;
  const lyricsPlay = document.getElementById('lyricsPlayBtn');
  if (lyricsPlay) lyricsPlay.innerHTML = isPlaying ? pauseSvg : playSvg;
}

function startPlaybackTimer() {
  stopPlaybackTimer();
  synthTimer = setInterval(() => {
    const track = currentVoiceTrack ? currentVoiceTrack : TRACKS[currentTrackIndex];
    currentTime += 1;
    if (currentTime >= track.duration) {
      if (isRepeat) {
        currentTime = 0;
      } else {
        if (currentVoiceTrack) {
          playNextVoiceNote();
        } else {
          playNextTrack();
        }
        return;
      }
    }
    document.getElementById('timeCurrent').textContent = formatTime(currentTime);
    document.getElementById('timeRemaining').textContent = formatTime(track.duration - currentTime);
    const pct = (currentTime / track.duration) * 100;
    updateScrubber(pct);
    if (!currentVoiceTrack) {
      syncLyricsWithTime(currentTime);
    }
  }, 1000);
}

function stopPlaybackTimer() {
  if (synthTimer) clearInterval(synthTimer);
}

function updateScrubber(percentage) {
  document.getElementById('scrubberFill').style.width = `${percentage}%`;
  document.getElementById('dockedProgressFill').style.width = `${percentage}%`;
}

function seekAudio(event) {
  const rect = event.currentTarget.getBoundingClientRect();
  const clickX = event.clientX - rect.left;
  const pct = Math.max(0, Math.min(1, clickX / rect.width));
  const track = currentVoiceTrack ? currentVoiceTrack : TRACKS[currentTrackIndex];
  currentTime = Math.floor(pct * track.duration);
  updateScrubber(pct * 100);
  document.getElementById('timeCurrent').textContent = formatTime(currentTime);
  document.getElementById('timeRemaining').textContent = formatTime(track.duration - currentTime);
  if (!currentVoiceTrack) {
    syncLyricsWithTime(currentTime);
  }
  playSynthBeep(440, 0.1);
}

function playNextTrack() {
  if (currentVoiceTrack) {
    playNextVoiceNote();
    return;
  }
  let nextIdx;
  if (isShuffle) {
    nextIdx = Math.floor(Math.random() * TRACKS.length);
  } else {
    nextIdx = (currentTrackIndex + 1) % TRACKS.length;
  }
  loadTrack(nextIdx);
  if (isPlaying) {
    playSynthBeep(660, 0.15);
    startPlaybackTimer();
  }
}

function playPrevTrack() {
  if (currentVoiceTrack) {
    playPrevVoiceNote();
    return;
  }
  if (currentTime > 4) {
    currentTime = 0;
    updateScrubber(0);
    return;
  }
  loadTrack(currentTrackIndex - 1);
  if (isPlaying) {
    playSynthBeep(440, 0.15);
    startPlaybackTimer();
  }
}

function toggleShuffle() {
  isShuffle = !isShuffle;
  document.getElementById('btnShuffle').classList.toggle('active', isShuffle);
  playSynthBeep(isShuffle ? 600 : 300, 0.1);
}

function toggleRepeat() {
  isRepeat = !isRepeat;
  document.getElementById('btnRepeat').classList.toggle('active', isRepeat);
  playSynthBeep(isRepeat ? 600 : 300, 0.1);
}

function toggleCurrentFavorite() {
  if (currentVoiceTrack) return;
  const track = TRACKS[currentTrackIndex];
  track.isFavorite = !track.isFavorite;
  const isFav = track.isFavorite;
  const favBtn = document.getElementById('fullPlayerFavBtn');
  if (favBtn) favBtn.style.color = isFav ? 'var(--favorite)' : 'var(--slate-muted)';
  playSynthBeep(isFav ? 700 : 350, 0.15);
  showGestureToast(isFav ? `❤️ Added "${track.title}" to Favorites` : `🤍 Removed "${track.title}" from Favorites`);

  updateFavoritesCounters();
  renderAllSongs();
  renderFavoritesScreen();
}

function playAllSongs(shuffleMode = false) {
  if (currentClassification === 'music' && currentMusicSubFilter === 'favorites') {
    playFavoritesCollection(shuffleMode);
    return;
  }
  isShuffle = shuffleMode;
  const btnShuffle = document.getElementById('btnShuffle');
  if (btnShuffle) btnShuffle.classList.toggle('active', isShuffle);

  if (currentClassification === 'music') {
    let targetTracks = TRACKS;
    if (currentMusicSubFilter === 'recent') {
      targetTracks = [...TRACKS].reverse();
    }
    const firstIdx = TRACKS.indexOf(targetTracks[0]);
    loadTrack(firstIdx);
  } else {
    loadTrack(0);
  }

  if (!isPlaying) togglePlayPause();
}

// ==========================================================================
// Full Player & Lyrics Modal Panels
// ==========================================================================
function openFullPlayer() {
  document.getElementById('fullPlayerPane').classList.add('open');
}

function closeFullPlayer() {
  document.getElementById('fullPlayerPane').classList.remove('open');
}

function openLyrics() {
  const pane = document.getElementById('lyricsPane');
  pane.classList.add('open');
  renderLyrics(TRACKS[currentTrackIndex]);
  setTimeout(() => {
    syncLyricsWithTime(currentTime);
  }, 100);
}

function closeLyrics() {
  document.getElementById('lyricsPane').classList.remove('open');
}

function renderLyrics(track) {
  const box = document.getElementById('lyricsScrollBox');
  if (!box) return;
  box.innerHTML = '';
  track.lyrics.forEach((line, idx) => {
    const div = document.createElement('div');
    div.className = 'lyric-line';
    div.id = `lyric-${idx}`;
    div.textContent = line.text;
    div.onclick = () => {
      currentTime = line.time;
      updateScrubber((currentTime / track.duration) * 100);
      syncLyricsWithTime(currentTime);
      playSynthBeep(580, 0.1);
    };
    box.appendChild(div);
  });
  
  const pane = document.getElementById('lyricsPane');
  if (pane && pane.classList.contains('open')) {
    syncLyricsWithTime(currentTime);
  }
}

function syncLyricsWithTime(time) {
  const lyricsPane = document.getElementById('lyricsPane');
  if (!lyricsPane || !lyricsPane.classList.contains('open')) {
    return;
  }
  const track = TRACKS[currentTrackIndex];
  if (!track || !track.lyrics) return;

  let activeIdx = -1;
  for (let i = 0; i < track.lyrics.length; i++) {
    if (time >= track.lyrics[i].time) {
      activeIdx = i;
    }
  }
  document.querySelectorAll('.lyric-line').forEach((el, idx) => {
    el.classList.toggle('active', idx === activeIdx);
  });
  const activeEl = document.getElementById(`lyric-${activeIdx}`);
  const scrollBox = document.getElementById('lyricsScrollBox');
  if (activeEl && scrollBox) {
    const targetScroll = activeEl.offsetTop - (scrollBox.clientHeight / 2) + (activeEl.clientHeight / 2);
    scrollBox.scrollTo({ top: Math.max(0, targetScroll), behavior: 'smooth' });
  }
}

// ==========================================================================
// Context Modal Bottom Sheet
// ==========================================================================
function openTrackMenu(trackIdx) {
  currentSelectedMenuTrack = trackIdx;
  const track = TRACKS[trackIdx];
  document.getElementById('sheetThumb').src = track.cover;
  document.getElementById('sheetTitle').textContent = track.title;
  document.getElementById('sheetArtist').textContent = track.artist;

  document.getElementById('sheetBackdrop').classList.add('open');
  document.getElementById('trackMenuSheet').classList.add('open');
}

function closeTrackMenu() {
  document.getElementById('sheetBackdrop').classList.remove('open');
  document.getElementById('trackMenuSheet').classList.remove('open');
}

function sheetPlayNow() {
  loadTrack(currentSelectedMenuTrack);
  if (!isPlaying) togglePlayPause();
  closeTrackMenu();
}

function sheetPlayNext() {
  playSynthBeep(600, 0.15);
  closeTrackMenu();
}

function sheetAddToQueue() {
  playSynthBeep(650, 0.15);
  closeTrackMenu();
}

function sheetAddToPlaylist() {
  playSynthBeep(700, 0.15);
  closeTrackMenu();
}

function sheetShare() {
  navigator.clipboard?.writeText?.(window.location.href);
  alert(`Shared: ${TRACKS[currentSelectedMenuTrack].title} by ${TRACKS[currentSelectedMenuTrack].artist}`);
  closeTrackMenu();
}

// ==========================================================================
// Dynamic Content Injection & Renderers
// ==========================================================================
function renderRecommended() {
  const row = document.getElementById('recommendedRow');
  if (!row) return;
  row.innerHTML = ALBUMS.map(album => `
    <div class="album-card" onclick="openAlbumDetail('${album.id}')">
      <div class="album-art-wrap">
        <img src="${album.cover}" alt="${album.title}">
        <div class="album-play-overlay">▶</div>
      </div>
      <div class="album-title">${album.title}</div>
      <div class="album-artist">${album.artist}</div>
    </div>
  `).join('');
}

function renderArtistsCarousel() {
  const row = document.getElementById('artistsRow');
  if (!row) return;
  row.innerHTML = ARTISTS.map(artist => `
    <div class="artist-circle-card" onclick="openArtistDetail('${artist.id}')">
      <img src="${artist.avatar}" class="artist-avatar" alt="${artist.name}">
      <span class="artist-name">${artist.name}</span>
      <span class="artist-sub">${artist.followers.split(' ')[0]}</span>
    </div>
  `).join('');
}

function switchSongClassification(type) {
  currentClassification = type;
  
  // Toggle music subfilter bar visibility (only visible in Pure Music)
  const subfilterBar = document.getElementById('musicSubfilterBar');
  if (subfilterBar) {
    subfilterBar.style.display = (type === 'music') ? 'flex' : 'none';
  }

  // Update Pills UI
  const pillMap = {
    'music': 'pillMusic',
    'whatsapp': 'pillWhatsApp',
    'recordings': 'pillRecordings'
  };
  document.querySelectorAll('.class-pill').forEach(btn => btn.classList.remove('active'));
  const activePill = document.getElementById(pillMap[type]);
  if (activePill) activePill.classList.add('active');

  const titleEl = document.getElementById('songsHeaderTitle');
  const countEl = document.getElementById('songsCountText');
  const actionRow = document.getElementById('songsActionRow');

  if (type === 'music') {
    if (titleEl) titleEl.textContent = (currentMusicSubFilter === 'favorites') ? 'Favorite Songs' : 'Pure Music';
    if (countEl) {
      if (currentMusicSubFilter === 'favorites') {
        const favCount = TRACKS.filter(t => t.isFavorite).length;
        countEl.textContent = `${favCount} Favorite Songs • Liked & Offline`;
      } else if (currentMusicSubFilter === 'recent') {
        countEl.textContent = `${TRACKS.length} Songs • Recently Added`;
      } else {
        countEl.textContent = '557 Songs • Filtered Music Only';
      }
    }
    if (actionRow) {
      actionRow.innerHTML = `
        <button class="btn-primary-pill" onclick="playAllSongs(false)">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor"><path d="M8 5v14l11-7z"/></svg>
          Play All
        </button>
        <button class="btn-secondary-pill" onclick="playAllSongs(true)">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor"><path d="M10.59 9.17L5.41 4 4 5.41l5.17 5.17 1.42-1.41zM14.5 4l2.04 2.04L4 18.59 5.41 20 17.96 7.46 20 9.5V4h-5.5zm.33 9.41l-1.41 1.41 3.13 3.13L14.5 20H20v-5.5l-2.04 2.04-3.13-3.13z"/></svg>
          Shuffle
        </button>
      `;
    }
  } else if (type === 'whatsapp') {
    if (titleEl) titleEl.textContent = 'WhatsApp Audio';
    if (countEl) countEl.textContent = `${WHATSAPP_VOICES.length} WhatsApp Notes • Excluded from Music`;
    if (actionRow) {
      actionRow.innerHTML = `
        <button class="btn-primary-pill" style="background: #25D366; box-shadow: 0 4px 14px rgba(37, 211, 102, 0.4);" onclick="playAllVoiceNotes()">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor"><path d="M8 5v14l11-7z"/></svg>
          Play All Notes
        </button>
        <button class="btn-secondary-pill" onclick="toggleVoiceSort()">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor"><path d="M3 18h6v-2H3v2zM3 6v2h18V6H3zm0 7h12v-2H3v2z"/></svg>
          <span id="voiceSortLabel">${voiceSortAsc ? 'Oldest First' : 'Date Sort'}</span>
        </button>
      `;
    }
  } else if (type === 'recordings') {
    if (titleEl) titleEl.textContent = 'Voice Recordings';
    if (countEl) countEl.textContent = `${RECORDINGS.length} Device Recordings • Excluded from Music`;
    if (actionRow) {
      actionRow.innerHTML = `
        <button class="btn-primary-pill" style="background: #ff3366; box-shadow: 0 4px 14px rgba(255, 51, 102, 0.4);" onclick="playAllVoiceNotes()">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor"><path d="M8 5v14l11-7z"/></svg>
          Play All
        </button>
        <button class="btn-secondary-pill" onclick="navigateTo('paneVoiceRecorder')">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="#ff3366"><path d="M12 14c1.66 0 3-1.34 3-3V5c0-1.66-1.34-3-3-3S9 3.34 9 5v6c0 1.66 1.34 3 3 3z"/></svg>
          New Memo
        </button>
      `;
    }
  }

  renderAllSongs();
  navigateTo('paneSongs');
}

function playVoiceItem(id, title, duration) {
  initAudioEngine();
  const activeList = currentClassification === 'whatsapp' ? WHATSAPP_VOICES : RECORDINGS;
  const item = activeList.find(v => v.id === id) || {
    id: id,
    title: title,
    duration: duration,
    sender: currentClassification === 'whatsapp' ? 'WhatsApp' : 'Voice Memo',
    location: 'Voice Recorder',
    date: 'Today'
  };

  // If already playing this voice note, toggle play/pause
  if (currentVoiceTrack && currentVoiceTrack.id === id) {
    togglePlayPause();
    renderAllSongs();
    return;
  }

  currentVoiceTrack = {
    id: item.id,
    title: item.title,
    artist: currentClassification === 'whatsapp' ? (item.sender || 'WhatsApp Voice Note') : (item.location || 'Device Memo'),
    album: currentClassification === 'whatsapp' ? 'WhatsApp Media' : 'Voice Recordings',
    duration: item.duration,
    cover: 'assets/logo.png',
    isVoice: true,
    category: currentClassification
  };

  currentTime = 0;
  isPlaying = true;

  // Update Mini Docked Player
  const dockedTitle = document.getElementById('dockedTitle');
  const dockedArtist = document.getElementById('dockedArtist');
  const dockedThumb = document.getElementById('dockedThumb');
  if (dockedTitle) dockedTitle.textContent = item.title;
  if (dockedArtist) dockedArtist.textContent = currentVoiceTrack.artist;
  if (dockedThumb) dockedThumb.src = 'assets/logo.png';

  // Update Full Player
  const fullTitle = document.getElementById('fullPlayerTitle');
  const fullArtist = document.getElementById('fullPlayerArtist');
  const fullCover = document.getElementById('fullPlayerCover');
  if (fullTitle) fullTitle.textContent = item.title;
  if (fullArtist) fullArtist.textContent = currentVoiceTrack.artist;
  if (fullCover) fullCover.src = 'assets/logo.png';

  document.getElementById('timeRemaining').textContent = formatTime(item.duration);
  document.getElementById('timeCurrent').textContent = "0:00";
  updateScrubber(0);

  updatePlayPauseUI();
  startPlaybackTimer();

  playSynthBeep(620, 0.25);
  showGestureToast(`▶ Playing: ${item.title}`);
  renderAllSongs();
}

function playAllVoiceNotes() {
  const rawList = currentClassification === 'whatsapp' ? WHATSAPP_VOICES : RECORDINGS;
  if (!rawList || rawList.length === 0) return;
  const list = voiceSortAsc ? [...rawList].reverse() : rawList;
  const first = list[0];
  playVoiceItem(first.id, first.title, first.duration);
}

function playNextVoiceNote() {
  const rawList = currentClassification === 'whatsapp' ? WHATSAPP_VOICES : RECORDINGS;
  if (!rawList || rawList.length === 0) return;
  const list = voiceSortAsc ? [...rawList].reverse() : rawList;
  const curIdx = list.findIndex(v => currentVoiceTrack && v.id === currentVoiceTrack.id);
  const nextIdx = (curIdx + 1) % list.length;
  const nextItem = list[nextIdx];
  playVoiceItem(nextItem.id, nextItem.title, nextItem.duration);
}

function playPrevVoiceNote() {
  const rawList = currentClassification === 'whatsapp' ? WHATSAPP_VOICES : RECORDINGS;
  if (!rawList || rawList.length === 0) return;
  const list = voiceSortAsc ? [...rawList].reverse() : rawList;
  if (currentTime > 3) {
    currentTime = 0;
    updateScrubber(0);
    return;
  }
  const curIdx = list.findIndex(v => currentVoiceTrack && v.id === currentVoiceTrack.id);
  const prevIdx = (curIdx - 1 + list.length) % list.length;
  const prevItem = list[prevIdx];
  playVoiceItem(prevItem.id, prevItem.title, prevItem.duration);
}

function toggleSortOrder() {
  const modes = ['recent', 'title', 'artist'];
  const labels = {
    'recent': 'Recently Added',
    'title': 'Title (A-Z)',
    'artist': 'Artist (A-Z)'
  };
  const curIdx = modes.indexOf(musicSortMode);
  musicSortMode = modes[(curIdx + 1) % modes.length];

  const labelEl = document.getElementById('sortOrderLabel');
  if (labelEl) {
    labelEl.textContent = labels[musicSortMode];
  }
  showGestureToast(`📅 Sort: ${labels[musicSortMode]}`);
  renderAllSongs();
}

function toggleVoiceSort() {
  voiceSortAsc = !voiceSortAsc;
  showGestureToast(voiceSortAsc ? '📅 Sorted: Oldest First' : '📅 Sorted: Newest First');
  const sortLabel = document.getElementById('voiceSortLabel');
  if (sortLabel) {
    sortLabel.textContent = voiceSortAsc ? 'Oldest First' : 'Date Sort';
  }
  renderAllSongs();
}

function setMusicSubFilter(subfilter) {
  currentMusicSubFilter = subfilter;
  
  document.querySelectorAll('.subfilter-chip').forEach(btn => btn.classList.remove('active'));
  const chipMap = {
    'all': 'subfilterAll',
    'favorites': 'subfilterFav',
    'recent': 'subfilterRecent'
  };
  const activeChip = document.getElementById(chipMap[subfilter]);
  if (activeChip) activeChip.classList.add('active');

  showGestureToast(
    subfilter === 'favorites' ? '❤️ Showing Favorite Songs' :
    subfilter === 'recent' ? '🕒 Showing Recently Added' : '🎵 Showing All Music'
  );

  renderAllSongs();
}

function openFavoritesScreen() {
  renderFavoritesScreen();
  navigateTo('paneFavorites');
}

function openSongsFavoritesFilter() {
  switchSongClassification('music');
  setMusicSubFilter('favorites');
  navigateTo('paneSongs');
}

function updateFavoritesCounters() {
  const favCount = TRACKS.filter(t => t.isFavorite).length;

  const subfilterFav = document.getElementById('countFavSongs');
  if (subfilterFav) subfilterFav.textContent = favCount;

  const allCountEl = document.getElementById('subfilterAllCount');
  if (allCountEl) allCountEl.textContent = TRACKS.length;

  const demoCount = document.getElementById('demoFavCount');
  if (demoCount) demoCount.textContent = favCount;

  const homeCount = document.getElementById('homeFavCount');
  if (homeCount) homeCount.textContent = favCount;

  const screenCount = document.getElementById('favTracksCount');
  if (screenCount) screenCount.textContent = favCount;

  const favSub = document.getElementById('favHeroSubtitle');
  if (favSub) favSub.textContent = `${favCount} Loved Songs • Always Ready Offline`;

  const favScreenMeta = document.getElementById('favScreenMeta');
  if (favScreenMeta) favScreenMeta.textContent = `${favCount} Favorite Songs • Quick Access`;
}

function renderFavoritesScreen() {
  const container = document.getElementById('favoritesScreenList');
  const favTracks = TRACKS.filter(t => t.isFavorite);

  updateFavoritesCounters();

  if (!container) return;

  if (favTracks.length === 0) {
    container.innerHTML = `
      <div class="empty-fav-state">
        <div class="empty-fav-icon">❤️</div>
        <div class="empty-fav-title">No Favorite Songs Yet</div>
        <div class="empty-fav-desc">Tap the heart icon on any track to add it to your favorite songs collection.</div>
        <button class="btn-primary-pill" style="margin-top: 8px; height: 38px; padding: 0 20px; font-size: 13px;" onclick="switchSongClassification('music')">Browse Pure Music</button>
      </div>
    `;
    return;
  }

  container.innerHTML = favTracks.map((track) => {
    const realIdx = TRACKS.indexOf(track);
    const isThisActive = !currentVoiceTrack && realIdx === currentTrackIndex;
    return `
      <div class="track-row ${isThisActive ? 'active' : ''}" onclick="selectTrack(${realIdx})">
        <img src="${track.cover}" class="track-thumb" alt="${track.title}">
        <div class="track-meta-stack">
          <div class="track-title">${track.title}</div>
          <div class="track-artist-duration">${track.artist} • ${formatTime(track.duration)}</div>
        </div>
        <div class="track-actions" onclick="event.stopPropagation()">
          <button class="icon-btn" onclick="toggleFavorite(${realIdx})" title="Remove from favorites">
            <span style="color: var(--favorite); font-size: 16px;">❤️</span>
          </button>
          <button class="icon-btn" onclick="openTrackMenu(${realIdx})">⋮</button>
        </div>
      </div>
    `;
  }).join('');
}

function playFavoritesCollection(shuffleMode = false) {
  const favs = TRACKS.filter(t => t.isFavorite);
  if (!favs.length) {
    showGestureToast('⚠️ No favorite songs yet');
    return;
  }
  isShuffle = shuffleMode;
  const btnShuffle = document.getElementById('btnShuffle');
  if (btnShuffle) btnShuffle.classList.toggle('active', isShuffle);

  let targetTrack = favs[0];
  if (shuffleMode && favs.length > 1) {
    targetTrack = favs[Math.floor(Math.random() * favs.length)];
  }
  selectTrack(TRACKS.indexOf(targetTrack));
  showGestureToast(shuffleMode ? '🔀 Shuffling Favorites' : '▶ Playing Favorites');
}

function renderAllSongs() {
  const list = document.getElementById('allSongsList');
  if (!list) return;

  updateFavoritesCounters();

  if (currentClassification === 'music') {
    let activeTracks = [...TRACKS];
    if (currentMusicSubFilter === 'favorites') {
      activeTracks = TRACKS.filter(t => t.isFavorite);
    } else if (currentMusicSubFilter === 'recent') {
      activeTracks = [...TRACKS].reverse();
    }

    // Apply sort mode
    if (musicSortMode === 'title') {
      activeTracks.sort((a, b) => a.title.localeCompare(b.title));
    } else if (musicSortMode === 'artist') {
      activeTracks.sort((a, b) => a.artist.localeCompare(b.artist));
    }

    const countEl = document.getElementById('songsCountText');
    const titleEl = document.getElementById('songsHeaderTitle');
    if (titleEl) {
      titleEl.textContent = currentMusicSubFilter === 'favorites' ? 'Favorite Songs' : 'Pure Music';
    }

    if (countEl) {
      if (currentMusicSubFilter === 'favorites') {
        countEl.textContent = `${activeTracks.length} Favorite Songs • Liked & Offline`;
      } else if (currentMusicSubFilter === 'recent') {
        countEl.textContent = `${activeTracks.length} Songs • Recently Added`;
      } else {
        countEl.textContent = '557 Songs • Filtered Music Only';
      }
    }

    if (activeTracks.length === 0) {
      if (currentMusicSubFilter === 'favorites') {
        list.innerHTML = `
          <div class="empty-fav-state">
            <div class="empty-fav-icon">❤️</div>
            <div class="empty-fav-title">No Favorite Songs Yet</div>
            <div class="empty-fav-desc">Tap the ❤️ heart icon on any song to add it to your favorites collection.</div>
            <button class="btn-primary-pill" style="margin-top: 8px; height: 38px; padding: 0 20px; font-size: 13px;" onclick="setMusicSubFilter('all')">Browse All Songs</button>
          </div>
        `;
        return;
      }
    }

    list.innerHTML = activeTracks.map((track) => {
      const realIdx = TRACKS.indexOf(track);
      const isThisActive = !currentVoiceTrack && realIdx === currentTrackIndex;
      return `
        <div class="track-row ${isThisActive ? 'active' : ''}" id="songRow-${realIdx}" onclick="selectTrack(${realIdx})">
          <img src="${track.cover}" class="track-thumb" alt="${track.title}">
          <div class="track-meta-stack">
            <div class="track-title">${track.title}</div>
            <div class="track-artist-duration">${track.artist} • ${formatTime(track.duration)}</div>
          </div>
          <div class="track-actions" onclick="event.stopPropagation()">
            <button class="icon-btn" onclick="toggleFavorite(${realIdx})" title="${track.isFavorite ? 'Remove from favorites' : 'Add to favorites'}">
              <span style="color: ${track.isFavorite ? 'var(--favorite)' : 'var(--slate-muted)'}; font-size: 16px;">
                ${track.isFavorite ? '❤️' : '🤍'}
              </span>
            </button>
            <button class="icon-btn" onclick="openTrackMenu(${realIdx})">⋮</button>
          </div>
        </div>
      `;
    }).join('');
  } else if (currentClassification === 'whatsapp') {
    const rawList = WHATSAPP_VOICES;
    const sortedList = voiceSortAsc ? [...rawList].reverse() : rawList;
    list.innerHTML = sortedList.map((item, idx) => {
      const isThisActive = currentVoiceTrack && currentVoiceTrack.id === item.id;
      const isThisPlaying = isThisActive && isPlaying;
      return `
      <div class="voice-item-card ${isThisActive ? 'active-playing' : ''}" onclick="playVoiceItem('${item.id}', '${item.title}', ${item.duration})">
        <div class="voice-icon-wrap whatsapp">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="currentColor"><path d="M12.04 2c-5.46 0-9.91 4.45-9.91 9.91 0 1.75.46 3.45 1.32 4.95L2.05 22l5.25-1.38c1.45.79 3.08 1.21 4.74 1.21 5.46 0 9.91-4.45 9.91-9.91 0-2.65-1.03-5.14-2.9-7.01A9.816 9.816 0 0012.04 2z"/></svg>
        </div>
        <div class="voice-meta-stack">
          <div class="voice-title">${item.title}</div>
          <div class="voice-details">
            <span class="voice-badge-tag whatsapp">WhatsApp</span>
            <span>${item.sender}</span>
            <span>•</span>
            <span>${formatTime(item.duration)}</span>
            <span>•</span>
            <span>${item.date}</span>
          </div>
          <div class="voice-waveform-preview">
            <span class="voice-wave-bar" style="height: 6px;"></span>
            <span class="voice-wave-bar" style="height: 12px;"></span>
            <span class="voice-wave-bar" style="height: 18px;"></span>
            <span class="voice-wave-bar" style="height: 10px;"></span>
            <span class="voice-wave-bar" style="height: 14px;"></span>
            <span class="voice-wave-bar" style="height: 8px;"></span>
            <span class="voice-wave-bar" style="height: 16px;"></span>
            <span class="voice-wave-bar" style="height: 6px;"></span>
          </div>
        </div>
        <button class="track-play-btn" style="background: #25D366; box-shadow: 0 4px 12px rgba(37, 211, 102, 0.4);" onclick="event.stopPropagation(); playVoiceItem('${item.id}', '${item.title}', ${item.duration})">
          ${isThisPlaying ? '⏸' : '▶'}
        </button>
      </div>
    `;
    }).join('');
  } else if (currentClassification === 'recordings') {
    const rawList = RECORDINGS;
    const sortedList = voiceSortAsc ? [...rawList].reverse() : rawList;
    list.innerHTML = sortedList.map((item, idx) => {
      const isThisActive = currentVoiceTrack && currentVoiceTrack.id === item.id;
      const isThisPlaying = isThisActive && isPlaying;
      return `
      <div class="voice-item-card recording ${isThisActive ? 'active-playing' : ''}" onclick="playVoiceItem('${item.id}', '${item.title}', ${item.duration})">
        <div class="voice-icon-wrap recording">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="currentColor"><path d="M12 14c1.66 0 3-1.34 3-3V5c0-1.66-1.34-3-3-3S9 3.34 9 5v6c0 1.66 1.34 3 3 3z"/></svg>
        </div>
        <div class="voice-meta-stack">
          <div class="voice-title">${item.title}</div>
          <div class="voice-details">
            <span class="voice-badge-tag recording">Recording</span>
            <span>${item.location}</span>
            <span>•</span>
            <span>${formatTime(item.duration)}</span>
            <span>•</span>
            <span>${item.date}</span>
          </div>
          <div class="voice-waveform-preview">
            <span class="voice-wave-bar" style="height: 14px;"></span>
            <span class="voice-wave-bar" style="height: 8px;"></span>
            <span class="voice-wave-bar" style="height: 16px;"></span>
            <span class="voice-wave-bar" style="height: 12px;"></span>
            <span class="voice-wave-bar" style="height: 18px;"></span>
            <span class="voice-wave-bar" style="height: 6px;"></span>
            <span class="voice-wave-bar" style="height: 10px;"></span>
          </div>
        </div>
        <button class="track-play-btn" style="background: #ff3366; box-shadow: 0 4px 12px rgba(255, 51, 102, 0.4);" onclick="event.stopPropagation(); playVoiceItem('${item.id}', '${item.title}', ${item.duration})">
          ${isThisPlaying ? '⏸' : '▶'}
        </button>
      </div>
    `;
    }).join('');
  }
}

function renderArtistsList() {
  const list = document.getElementById('artistsListContainer');
  list.innerHTML = ARTISTS.map(artist => `
    <div class="track-row" onclick="openArtistDetail('${artist.id}')">
      <img src="${artist.avatar}" style="width: 52px; height: 52px; border-radius: 50%; object-fit: cover;">
      <div class="track-meta-stack">
        <div class="track-title">${artist.name}</div>
        <div class="track-artist-duration">${artist.followers} • ${artist.songsCount} Songs</div>
      </div>
      <div class="track-actions">
        <button class="btn-secondary-pill" style="height: 36px; padding: 0 16px; font-size: 13px;">View</button>
      </div>
    </div>
  `).join('');
}

function renderAlbumsGrid() {
  const grid = document.getElementById('albumsGridContainer');
  grid.innerHTML = ALBUMS.map(album => `
    <div class="album-card" style="width: 100%;" onclick="openAlbumDetail('${album.id}')">
      <div class="album-art-wrap" style="width: 100%; height: 160px;">
        <img src="${album.cover}" alt="${album.title}">
        <div class="album-play-overlay">▶</div>
      </div>
      <div class="album-title">${album.title}</div>
      <div class="album-artist">${album.artist} • ${album.year}</div>
    </div>
  `).join('');
}

function renderFolders() {
  const container = document.getElementById('foldersListContainer');
  container.innerHTML = FOLDERS.map(folder => `
    <div class="track-row" onclick="openFolderDetail('${folder.id}')">
      <div style="width: 48px; height: 48px; border-radius: 12px; background: var(--surface-container); display: flex; align-items: center; justify-content: center; font-size: 24px; color: var(--primary);">
        📁
      </div>
      <div class="track-meta-stack">
        <div class="track-title">${folder.name}</div>
        <div class="track-artist-duration">${folder.count} audio files • ${folder.path}</div>
      </div>
      <div class="track-actions">
        <span style="color: var(--slate-muted); font-size: 18px;">›</span>
      </div>
    </div>
  `).join('');
}

function renderRecentlyPlayed() {
  const container = document.getElementById('recentlyPlayedFullList');
  const recentTracks = historyQueue.length ? historyQueue.map(id => TRACKS.find(t => t.id === id)).filter(Boolean) : TRACKS.slice(0, 4);
  container.innerHTML = recentTracks.map((track, idx) => `
    <div class="track-row" onclick="selectTrack(${TRACKS.indexOf(track)})">
      <img src="${track.cover}" class="track-thumb" alt="${track.title}">
      <div class="track-meta-stack">
        <div class="track-title">${track.title}</div>
        <div class="track-artist-duration">${track.artist} • ${formatTime(track.duration)}</div>
      </div>
      <div class="track-actions" onclick="event.stopPropagation()">
        <button class="track-play-btn" onclick="selectTrack(${TRACKS.indexOf(track)})">▶</button>
      </div>
    </div>
  `).join('');
}

function clearRecentlyPlayed() {
  historyQueue = [];
  renderRecentlyPlayed();
}

function selectTrack(idx) {
  loadTrack(idx);
  if (!isPlaying) togglePlayPause();
}

function highlightActiveTrack() {
  document.querySelectorAll('.track-row').forEach((row, idx) => {
    row.classList.toggle('active', idx === currentTrackIndex);
  });
}

function toggleFavorite(idx) {
  TRACKS[idx].isFavorite = !TRACKS[idx].isFavorite;
  const isFav = TRACKS[idx].isFavorite;
  playSynthBeep(isFav ? 700 : 350, 0.15);
  showGestureToast(isFav ? `❤️ Added "${TRACKS[idx].title}" to Favorites` : `🤍 Removed "${TRACKS[idx].title}" from Favorites`);

  updateFavoritesCounters();
  renderAllSongs();
  renderFavoritesScreen();

  if (idx === currentTrackIndex) {
    const favBtn = document.getElementById('fullPlayerFavBtn');
    if (favBtn) favBtn.style.color = isFav ? 'var(--favorite)' : 'var(--slate-muted)';
  }
}

// ==========================================================================
// Drill-Down Screens: Artist, Album, Folder Details
// ==========================================================================
let currentArtistSongs = [];
function openArtistDetail(artistId) {
  const artist = ARTISTS.find(a => a.id === artistId) || ARTISTS[0];
  document.getElementById('artistDetailAvatar').src = artist.avatar;
  document.getElementById('artistDetailName').textContent = artist.name;
  document.getElementById('artistDetailFollowers').textContent = `${artist.followers} • ${artist.songsCount} Songs`;
  document.getElementById('artistDetailHeaderTitle').textContent = artist.name;

  currentArtistSongs = TRACKS.filter(t => t.artist.toLowerCase().includes(artist.name.toLowerCase()));
  if (!currentArtistSongs.length) currentArtistSongs = TRACKS.slice(0, 3);

  const container = document.getElementById('artistSongsList');
  container.innerHTML = currentArtistSongs.map((track, i) => `
    <div class="track-row" onclick="selectTrack(${TRACKS.indexOf(track)})">
      <span style="font-weight: 700; color: var(--slate-muted); width: 20px;">0${i+1}</span>
      <img src="${track.cover}" class="track-thumb" alt="${track.title}">
      <div class="track-meta-stack">
        <div class="track-title">${track.title}</div>
        <div class="track-artist-duration">${formatTime(track.duration)}</div>
      </div>
      <button class="track-play-btn" onclick="selectTrack(${TRACKS.indexOf(track)})">▶</button>
    </div>
  `).join('');

  navigateTo('paneArtistDetail');
}

function playCurrentArtistSongs(shuffle) {
  if (currentArtistSongs.length) {
    const idx = TRACKS.indexOf(currentArtistSongs[0]);
    isShuffle = shuffle;
    selectTrack(idx);
  }
}

let currentAlbumSongs = [];
function openAlbumDetail(albumId) {
  const album = ALBUMS.find(a => a.id === albumId) || ALBUMS[0];
  document.getElementById('albumDetailCover').src = album.cover;
  document.getElementById('albumDetailTitle').textContent = album.title;
  document.getElementById('albumDetailArtist').textContent = `${album.artist} • ${album.year} • ${album.tracks} Tracks`;

  currentAlbumSongs = TRACKS.filter(t => t.album.toLowerCase() === album.title.toLowerCase());
  if (!currentAlbumSongs.length) currentAlbumSongs = TRACKS.slice(0, 3);

  const container = document.getElementById('albumSongsList');
  container.innerHTML = currentAlbumSongs.map((track, i) => `
    <div class="track-row" onclick="selectTrack(${TRACKS.indexOf(track)})">
      <span style="font-weight: 700; color: var(--slate-muted); width: 20px;">0${i+1}</span>
      <div class="track-meta-stack">
        <div class="track-title">${track.title}</div>
        <div class="track-artist-duration">${track.artist} • ${formatTime(track.duration)}</div>
      </div>
      <button class="track-play-btn" onclick="selectTrack(${TRACKS.indexOf(track)})">▶</button>
    </div>
  `).join('');

  navigateTo('paneAlbumDetail');
}

function playCurrentAlbumSongs(shuffle) {
  if (currentAlbumSongs.length) {
    const idx = TRACKS.indexOf(currentAlbumSongs[0]);
    isShuffle = shuffle;
    selectTrack(idx);
  }
}

function openFolderDetail(folderId) {
  const folder = FOLDERS.find(f => f.id === folderId) || FOLDERS[0];
  document.getElementById('folderDetailName').textContent = folder.name;
  document.getElementById('folderDetailMeta').textContent = `${folder.count} Songs • ${folder.path}`;

  const container = document.getElementById('folderSongsList');
  container.innerHTML = TRACKS.map((track, i) => `
    <div class="track-row" onclick="selectTrack(${i})">
      <img src="${track.cover}" class="track-thumb" alt="${track.title}">
      <div class="track-meta-stack">
        <div class="track-title">${track.title}</div>
        <div class="track-artist-duration">${folder.name} • ${formatTime(track.duration)}</div>
      </div>
      <button class="track-play-btn" onclick="selectTrack(${i})">▶</button>
    </div>
  `).join('');

  navigateTo('paneFolderDetail');
}

function playCurrentFolderSongs(shuffle) {
  isShuffle = shuffle;
  selectTrack(0);
}

// ==========================================================================
// Search Screen Logic
// ==========================================================================
function handleSearchInput(query) {
  const list = document.getElementById('searchResultsList');
  if (!query.trim()) {
    list.innerHTML = '';
    return;
  }
  const q = query.toLowerCase().trim();
  const matched = TRACKS.filter(t => t.title.toLowerCase().includes(q) || t.artist.toLowerCase().includes(q) || t.album.toLowerCase().includes(q));

  if (!matched.length) {
    list.innerHTML = `<div style="padding: 24px; text-align: center; color: var(--slate-muted);">No songs found matching "${query}"</div>`;
    return;
  }

  list.innerHTML = matched.map(track => `
    <div class="track-row" onclick="selectTrack(${TRACKS.indexOf(track)})">
      <img src="${track.cover}" class="track-thumb" alt="${track.title}">
      <div class="track-meta-stack">
        <div class="track-title">${track.title}</div>
        <div class="track-artist-duration">${track.artist} • ${formatTime(track.duration)}</div>
      </div>
      <button class="track-play-btn" onclick="selectTrack(${TRACKS.indexOf(track)})">▶</button>
    </div>
  `).join('');
}

function setSearch(term) {
  const input = document.getElementById('searchInput');
  input.value = term;
  handleSearchInput(term);
}

function clearSearch() {
  document.getElementById('searchInput').value = '';
  document.getElementById('searchResultsList').innerHTML = '';
}

// ==========================================================================
// View Mode Switcher: Phone vs Matrix Grid
// ==========================================================================
document.getElementById('btnModePhone').addEventListener('click', () => {
  document.getElementById('btnModePhone').classList.add('active');
  document.getElementById('btnModeGrid').classList.remove('active');
  document.getElementById('phoneStage').style.display = 'flex';
  document.getElementById('matrixContainer').classList.remove('active');
});

document.getElementById('btnModeGrid').addEventListener('click', () => {
  document.getElementById('btnModeGrid').classList.add('active');
  document.getElementById('btnModePhone').classList.remove('active');
  document.getElementById('phoneStage').style.display = 'none';
  document.getElementById('matrixContainer').classList.add('active');
});

function showGestureToast(msg) {
  const toast = document.getElementById('gestureToast');
  if (!toast) return;
  toast.textContent = msg;
  toast.classList.add('show');
  clearTimeout(toast._timer);
  toast._timer = setTimeout(() => {
    toast.classList.remove('show');
  }, 1400);
}

let touchStartX = 0;
let touchStartY = 0;
let mouseStartX = 0;
let isMouseDown = false;

function setupSwipeGestures() {
  const phone = document.querySelector('.device-phone');
  if (!phone) return;

  // Touch Swipe for Mobile / Emulation
  phone.addEventListener('touchstart', (e) => {
    touchStartX = e.changedTouches[0].clientX;
    touchStartY = e.changedTouches[0].clientY;
  }, { passive: true });

  phone.addEventListener('touchend', (e) => {
    const deltaX = e.changedTouches[0].clientX - touchStartX;
    const deltaY = e.changedTouches[0].clientY - touchStartY;
    if (Math.abs(deltaX) > 45 && Math.abs(deltaX) > Math.abs(deltaY) * 1.4) {
      if (deltaX > 0) {
        // Swipe Right -> Next track (forward)
        playNextTrack();
        showGestureToast("▶ Next Track (Swipe Right)");
      } else {
        // Swipe Left -> Previous track (backward)
        playPrevTrack();
        showGestureToast("◀ Previous Track (Swipe Left)");
      }
    }
  }, { passive: true });

  // Mouse drag support for desktop simulator
  phone.addEventListener('mousedown', (e) => {
    if (e.target.closest('button') || e.target.closest('input')) return;
    mouseStartX = e.clientX;
    isMouseDown = true;
  });

  phone.addEventListener('mouseup', (e) => {
    if (!isMouseDown) return;
    isMouseDown = false;
    const deltaX = e.clientX - mouseStartX;
    if (Math.abs(deltaX) > 60) {
      if (deltaX > 0) {
        playNextTrack();
        showGestureToast("▶ Next Track (Swipe Right)");
      } else {
        playPrevTrack();
        showGestureToast("◀ Previous Track (Swipe Left)");
      }
    }
  });
}

// ==========================================================================
// Custom Equalizer Engine & Interface (Sonic Tangerine DSP)
// ==========================================================================
const EQ_BANDS = [
  { freq: 60, label: "60Hz", name: "Sub-Bass", type: "lowshelf" },
  { freq: 150, label: "150Hz", name: "Bass", type: "peaking" },
  { freq: 400, label: "400Hz", name: "Low-Mid", type: "peaking" },
  { freq: 1000, label: "1kHz", name: "Midrange", type: "peaking" },
  { freq: 2400, label: "2.4k", name: "Upper-Mid", type: "peaking" },
  { freq: 6000, label: "6kHz", name: "Presence", type: "peaking" },
  { freq: 15000, label: "15k", name: "Air", type: "highshelf" }
];

const EQ_PRESETS = {
  custom: [4, 3, 0, 1, 3, 5, 4],
  flat: [0, 0, 0, 0, 0, 0, 0],
  bass_boost: [8, 6, 3, 0, -1, 1, 3],
  electronic: [7, 5, 1, 2, 4, 6, 7],
  rock: [6, 4, -2, 1, 4, 6, 5],
  hiphop: [8, 7, 2, 1, 0, 3, 5],
  vocal: [-2, 0, 3, 6, 7, 5, 2],
  pop: [2, 4, 5, 4, 2, 4, 5],
  acoustic: [4, 3, 2, 2, 4, 5, 5],
  jazz: [3, 2, 1, 3, 2, 4, 3]
};

let eqState = {
  enabled: true,
  currentPreset: 'custom',
  bands: [4, 3, 0, 1, 3, 5, 4],
  bassBoost: 45,
  virtualizer: 60,
  clarity: 30,
  reverb: 'studio'
};

let previousPaneBeforeEq = 'paneHome';
let biquadFilters = [];

function initEqualizer() {
  try {
    const saved = localStorage.getItem('bm_custom_eq');
    if (saved) {
      const parsed = JSON.parse(saved);
      if (Array.isArray(parsed.bands)) {
        eqState.bands = parsed.bands;
        EQ_PRESETS.custom = [...parsed.bands];
      }
    }
  } catch (e) {}

  renderEqualizerSliders();
  updateEqCurveSvg();
}

function openEqualizerScreen() {
  const activePane = document.querySelector('.screen-pane.active');
  if (activePane && activePane.id !== 'paneEqualizer') {
    previousPaneBeforeEq = activePane.id;
  }
  closeFullPlayer();
  navigateTo('paneEqualizer');
  renderEqualizerSliders();
  updateEqCurveSvg();
}

function closeEqualizerScreen() {
  navigateTo(previousPaneBeforeEq || 'paneHome');
}

function openEqualizerFromMenu() {
  closeTrackMenu();
  openEqualizerScreen();
}

function renderEqualizerSliders() {
  const board = document.getElementById('eqSlidersBoard');
  if (!board) return;

  board.innerHTML = EQ_BANDS.map((band, idx) => {
    const db = eqState.bands[idx] || 0;
    const sign = db > 0 ? `+${db}` : `${db}`;
    const pct = ((db + 12) / 24) * 100;
    const fillBottom = db >= 0 ? 50 : pct;
    const fillHeight = Math.abs(pct - 50);

    return `
      <div class="eq-band-col" data-index="${idx}">
        <div class="eq-band-val" id="eqVal-${idx}">${sign} dB</div>
        <div class="eq-track-groove" id="eqGroove-${idx}">
          <div class="eq-track-center-tick"></div>
          <div class="eq-track-fill" id="eqFill-${idx}" style="bottom: ${fillBottom}%; height: ${fillHeight}%;"></div>
          <div class="eq-track-thumb" id="eqThumb-${idx}" style="bottom: ${pct}%;"></div>
        </div>
        <div class="eq-band-label">${band.label}</div>
      </div>
    `;
  }).join('');

  EQ_BANDS.forEach((_, idx) => {
    const groove = document.getElementById(`eqGroove-${idx}`);
    if (!groove) return;

    const handleMove = (clientY) => {
      if (!eqState.enabled) return;
      const rect = groove.getBoundingClientRect();
      const relativeY = clientY - rect.top;
      let ratio = 1 - (relativeY / rect.height);
      ratio = Math.max(0, Math.min(1, ratio));
      const db = Math.round(ratio * 24 - 12);
      setBandLevel(idx, db);
    };

    groove.addEventListener('mousedown', (e) => {
      e.preventDefault();
      handleMove(e.clientY);
      const onMouseMove = (moveEvent) => handleMove(moveEvent.clientY);
      const onMouseUp = () => {
        window.removeEventListener('mousemove', onMouseMove);
        window.removeEventListener('mouseup', onMouseUp);
      };
      window.addEventListener('mousemove', onMouseMove);
      window.addEventListener('mouseup', onMouseUp);
    });

    groove.addEventListener('touchstart', (e) => {
      e.preventDefault();
      handleMove(e.touches[0].clientY);
      const onTouchMove = (moveEvent) => handleMove(moveEvent.touches[0].clientY);
      const onTouchEnd = () => {
        window.removeEventListener('touchmove', onTouchMove);
        window.removeEventListener('touchend', onTouchEnd);
      };
      window.addEventListener('touchmove', onTouchMove, { passive: false });
      window.addEventListener('touchend', onTouchEnd);
    }, { passive: false });
  });
}

function setBandLevel(index, db) {
  eqState.bands[index] = db;
  EQ_PRESETS.custom = [...eqState.bands];

  const valEl = document.getElementById(`eqVal-${index}`);
  const fillEl = document.getElementById(`eqFill-${index}`);
  const thumbEl = document.getElementById(`eqThumb-${index}`);
  
  const sign = db > 0 ? `+${db}` : `${db}`;
  if (valEl) valEl.textContent = `${sign} dB`;

  const pct = ((db + 12) / 24) * 100;
  const fillBottom = db >= 0 ? 50 : pct;
  const fillHeight = Math.abs(pct - 50);

  if (fillEl) {
    fillEl.style.bottom = `${fillBottom}%`;
    fillEl.style.height = `${fillHeight}%`;
  }
  if (thumbEl) {
    thumbEl.style.bottom = `${pct}%`;
  }

  setActivePresetPill('custom');
  updateEqCurveSvg();
  applyFiltersToAudioContext();
}

function updateEqCurveSvg() {
  const strokePath = document.getElementById('eqCurveStroke');
  const fillPath = document.getElementById('eqCurveFill');
  if (!strokePath || !fillPath) return;

  const points = EQ_BANDS.map((_, i) => {
    const x = 20 + i * ((320 - 40) / 6);
    const db = eqState.enabled ? eqState.bands[i] : 0;
    const y = 55 - (db / 12) * 42;
    return { x, y };
  });

  let d = `M ${points[0].x} ${points[0].y}`;
  for (let i = 0; i < points.length - 1; i++) {
    const p0 = points[i === 0 ? 0 : i - 1];
    const p1 = points[i];
    const p2 = points[i + 1];
    const p3 = points[i + 2] || p2;

    const cp1x = p1.x + (p2.x - p0.x) / 6;
    const cp1y = p1.y + (p2.y - p0.y) / 6;
    const cp2x = p2.x - (p3.x - p1.x) / 6;
    const cp2y = p2.y - (p3.y - p1.y) / 6;

    d += ` C ${cp1x.toFixed(1)} ${cp1y.toFixed(1)}, ${cp2x.toFixed(1)} ${cp2y.toFixed(1)}, ${p2.x.toFixed(1)} ${p2.y.toFixed(1)}`;
  }

  strokePath.setAttribute('d', d);
  strokePath.setAttribute('stroke', eqState.enabled ? '#ff7a00' : '#4a4d57');

  const fillD = `${d} L 320 110 L 0 110 Z`;
  fillPath.setAttribute('d', fillD);
  fillPath.style.opacity = eqState.enabled ? '1' : '0.15';
}

function applyEqPreset(presetName) {
  if (!EQ_PRESETS[presetName]) return;
  eqState.currentPreset = presetName;
  eqState.bands = [...EQ_PRESETS[presetName]];

  setActivePresetPill(presetName);
  renderEqualizerSliders();
  updateEqCurveSvg();
  applyFiltersToAudioContext();

  const formattedName = presetName.replace('_', ' ').toUpperCase();
  const badge = document.getElementById('activePresetBadge');
  if (badge) badge.textContent = `${formattedName} Profile`;
  showGestureToast(`🎚️ Preset: ${formattedName}`);
}

function setActivePresetPill(presetName) {
  document.querySelectorAll('.eq-preset-pill').forEach(btn => btn.classList.remove('active'));
  const activeBtn = document.getElementById(`presetPill-${presetName}`);
  if (activeBtn) activeBtn.classList.add('active');
  const badge = document.getElementById('activePresetBadge');
  if (badge) badge.textContent = `${presetName.replace('_', ' ')} Profile`;
}

function toggleEqPower(enabled) {
  eqState.enabled = enabled;
  const statusEl = document.getElementById('eqPowerStatus');
  if (statusEl) {
    statusEl.textContent = enabled ? 'Active • 7-Band Precision Filter' : 'Bypassed • Raw Audio Output';
    statusEl.style.color = enabled ? 'var(--primary-light)' : 'var(--slate-muted)';
  }
  updateEqCurveSvg();
  applyFiltersToAudioContext();
  showGestureToast(enabled ? "🎚️ DSP Equalizer Enabled" : "Bypassed Equalizer");
}

function updateBassBoost(val) {
  eqState.bassBoost = parseInt(val, 10);
  const el = document.getElementById('valBassBoost');
  if (el) el.textContent = `${val}%`;
  applyFiltersToAudioContext();
}

function updateVirtualizer(val) {
  eqState.virtualizer = parseInt(val, 10);
  const el = document.getElementById('valVirtualizer');
  if (el) el.textContent = `${val}%`;
  applyFiltersToAudioContext();
}

function updateClarity(val) {
  eqState.clarity = parseInt(val, 10);
  const el = document.getElementById('valClarity');
  if (el) el.textContent = `${val}%`;
  applyFiltersToAudioContext();
}

function setReverb(type) {
  eqState.reverb = type;
  document.querySelectorAll('.eq-reverb-pill').forEach(btn => btn.classList.remove('active'));
  const map = {
    'studio': 'revStudio',
    'hall': 'revHall',
    'arena': 'revArena',
    'none': 'revNone'
  };
  const activeBtn = document.getElementById(map[type]);
  if (activeBtn) activeBtn.classList.add('active');
  showGestureToast(`Reverb: ${type.toUpperCase()}`);
}

function resetEqualizer() {
  applyEqPreset('flat');
  eqState.bassBoost = 0;
  eqState.virtualizer = 0;
  eqState.clarity = 0;
  const bEl = document.getElementById('sliderBassBoost');
  if (bEl) bEl.value = 0;
  const vEl = document.getElementById('sliderVirtualizer');
  if (vEl) vEl.value = 0;
  const cEl = document.getElementById('sliderClarity');
  if (cEl) cEl.value = 0;
  updateBassBoost(0);
  updateVirtualizer(0);
  updateClarity(0);
  showGestureToast("Equalizer Reset to 0 dB Flat");
}

function saveCustomPreset() {
  try {
    localStorage.setItem('bm_custom_eq', JSON.stringify({ bands: eqState.bands }));
    showGestureToast("✓ Custom Equalizer Profile Saved!");
  } catch (e) {
    showGestureToast("Profile Saved in Session");
  }
}

function applyFiltersToAudioContext() {
  if (!audioContext || biquadFilters.length === 0) return;
  EQ_BANDS.forEach((band, idx) => {
    if (biquadFilters[idx]) {
      const gain = eqState.enabled ? eqState.bands[idx] : 0;
      biquadFilters[idx].gain.setValueAtTime(gain, audioContext.currentTime);
    }
  });
}

function auditionEqualizer() {
  initAudioEngine();
  if (!audioContext) return;
  if (audioContext.state === 'suspended') {
    audioContext.resume();
  }

  const notes = [
    { freq: 130.81, time: 0, dur: 0.8 },    // C3 (Bass test)
    { freq: 196.00, time: 0.15, dur: 0.8 }, // G3 (Low-mid)
    { freq: 261.63, time: 0.3, dur: 0.8 },  // C4 (Mid)
    { freq: 329.63, time: 0.45, dur: 0.8 }, // E4 (High-mid)
    { freq: 392.00, time: 0.6, dur: 0.8 },  // G4 (Treble)
    { freq: 523.25, time: 0.75, dur: 1.2 }  // C5 (Air presence)
  ];

  notes.forEach(note => {
    const osc = audioContext.createOscillator();
    const noteGain = audioContext.createGain();

    osc.type = 'sawtooth';
    osc.frequency.setValueAtTime(note.freq, audioContext.currentTime + note.time);

    const bassFactor = note.freq < 200 ? (1 + (eqState.bassBoost / 100) * 0.8) : 1;
    const trebleFactor = note.freq > 400 ? (1 + (eqState.clarity / 100) * 0.6) : 1;

    const vol = (0.08 * bassFactor * trebleFactor);
    noteGain.gain.setValueAtTime(vol, audioContext.currentTime + note.time);
    noteGain.gain.exponentialRampToValueAtTime(0.001, audioContext.currentTime + note.time + note.dur);

    const bandFilter = audioContext.createBiquadFilter();
    bandFilter.type = 'peaking';
    bandFilter.frequency.value = note.freq;

    let closestIndex = 0;
    let minDiff = Infinity;
    EQ_BANDS.forEach((b, idx) => {
      const diff = Math.abs(b.freq - note.freq);
      if (diff < minDiff) {
        minDiff = diff;
        closestIndex = idx;
      }
    });
    bandFilter.gain.value = eqState.enabled ? eqState.bands[closestIndex] : 0;

    osc.onended = () => {
      try {
        osc.disconnect();
        bandFilter.disconnect();
        noteGain.disconnect();
      } catch (_) {}
    };

    osc.start(audioContext.currentTime + note.time);
    osc.stop(audioContext.currentTime + note.time + note.dur);
  });

  showGestureToast("🔊 Auditioning Custom Equalizer");
}

// ==========================================================================
// Page Visibility API - Battery & Low CPU Throttle
// ==========================================================================
document.addEventListener('visibilitychange', () => {
  if (document.hidden) {
    // When tab/app is minimized or screen off, stop 1-second UI loops
    if (isPlaying) {
      stopPlaybackTimer();
      // Low-power background ticker (updates track time every 10s)
      if (!bgVisibilityTimer) {
        bgVisibilityTimer = setInterval(() => {
          const track = TRACKS[currentTrackIndex];
          currentTime += 10;
          if (currentTime >= track.duration) {
            playNextTrack();
          }
        }, 10000);
      }
    }
  } else {
    // Restore full UI loop on resume
    if (bgVisibilityTimer) {
      clearInterval(bgVisibilityTimer);
      bgVisibilityTimer = null;
    }
    if (isPlaying) {
      startPlaybackTimer();
    }
    // Instant UI refresh
    const track = TRACKS[currentTrackIndex];
    document.getElementById('timeCurrent').textContent = formatTime(currentTime);
    document.getElementById('timeRemaining').textContent = formatTime(track.duration - currentTime);
    updateScrubber((currentTime / track.duration) * 100);
    syncLyricsWithTime(currentTime);
  }
});

// ==========================================================================
// Bootstrapping
// ==========================================================================
window.addEventListener('DOMContentLoaded', () => {
  renderRecommended();
  renderArtistsCarousel();
  renderMostPlayed();
  renderAllSongs();
  renderFavoritesScreen();
  updateFavoritesCounters();
  renderArtistsList();
  renderAlbumsGrid();
  renderFolders();
  initEqualizer();
  loadTrack(0);
  setupSwipeGestures();

  const phone = document.querySelector('.device-phone');
  if (phone) phone.scrollTop = 0;
});
