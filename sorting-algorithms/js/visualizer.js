/**
 * ============================================
 * 排序算法可视化 - 主控制逻辑
 * ============================================
 */

// DOM 引用
const barsContainer = document.getElementById('bars-container');
const algorithmSelect = document.getElementById('algorithm');
const sizeSlider = document.getElementById('size');
const sizeValue = document.getElementById('sizeValue');
const speedSlider = document.getElementById('speed');
const speedValue = document.getElementById('speedValue');
const generateBtn = document.getElementById('generateBtn');
const sortBtn = document.getElementById('sortBtn');
const stopBtn = document.getElementById('stopBtn');
const comparisonsEl = document.getElementById('comparisons');
const swapsEl = document.getElementById('swaps');
const timeEl = document.getElementById('time');
const statusEl = document.getElementById('status');
const algoNameEl = document.getElementById('algoName');
const timeComplexityEl = document.getElementById('timeComplexity');
const spaceComplexityEl = document.getElementById('spaceComplexity');
const stabilityEl = document.getElementById('stability');
const algoDescriptionEl = document.getElementById('algoDescription');
const algoCodeEl = document.getElementById('algoCode');

// 状态
let array = [];
let animationId = null;
let isRunning = false;
let isStopped = false;
let stats = { comparisons: 0, swaps: 0 };

// ======================== 速度映射 ========================
function getDelay(speed) {
  const map = {
    1: 200, 2: 100, 3: 50, 4: 30, 5: 15,
    6: 8, 7: 4, 8: 2, 9: 1, 10: 0
  };
  return map[speed] || 15;
}

function getSpeedLabel(speed) {
  const labels = {
    1: '极慢', 2: '很慢', 3: '慢', 4: '较慢', 5: '中等',
    6: '较快', 7: '快', 8: '很快', 9: '极快', 10: '瞬间'
  };
  return labels[speed] || '中等';
}

// ======================== 数组生成 ========================
function generateArray(size) {
  array = [];
  for (let i = 0; i < size; i++) {
    array.push(Math.floor(Math.random() * 350) + 10);
  }
}

// ======================== 渲染柱状图 ========================
function renderBars(arr, highlights = {}) {
  barsContainer.innerHTML = '';
  const maxVal = Math.max(...arr);
  const barCount = arr.length;

  arr.forEach((val, idx) => {
    const bar = document.createElement('div');
    bar.className = 'bar';
    const heightPct = (val / maxVal) * 100;
    bar.style.height = `${Math.max(heightPct, 2)}%`;

    // 显示数值（数据少时）
    if (barCount <= 30) {
      bar.textContent = val;
      bar.style.display = 'flex';
      bar.style.alignItems = 'flex-end';
      bar.style.justifyContent = 'center';
      bar.style.fontSize = '10px';
      bar.style.color = 'rgba(255,255,255,0.8)';
      bar.style.paddingBottom = '2px';
    }

    barsContainer.appendChild(bar);
  });

  // 应用高亮
  if (highlights.comparing) {
    highlights.comparing.forEach(i => {
      const bar = barsContainer.children[i];
      if (bar) bar.classList.add('comparing');
    });
  }
  if (highlights.swapping) {
    highlights.swapping.forEach(i => {
      const bar = barsContainer.children[i];
      if (bar) bar.classList.add('swapping');
    });
  }
  if (highlights.sorted) {
    highlights.sorted.forEach(i => {
      const bar = barsContainer.children[i];
      if (bar) bar.classList.add('sorted');
    });
  }
  if (highlights.pivot) {
    highlights.pivot.forEach(i => {
      const bar = barsContainer.children[i];
      if (bar) bar.classList.add('pivot');
    });
  }
}

// ======================== 统计信息更新 ========================
function updateStats(comparisons, swaps) {
  comparisonsEl.textContent = comparisons;
  swapsEl.textContent = swaps;
}

function setStatus(text, className) {
  statusEl.textContent = text;
  statusEl.className = 'stat-value ' + (className || 'status-idle');
}

// ======================== 算法信息更新 ========================
function updateAlgorithmInfo(algoKey) {
  const algo = ALGORITHMS[algoKey];
  if (!algo) return;
  algoNameEl.textContent = algo.name;
  timeComplexityEl.textContent = `时间复杂度: ${algo.time}`;
  spaceComplexityEl.textContent = `空间复杂度: ${algo.space}`;
  stabilityEl.textContent = `稳定性: ${algo.stable ? '稳定 ⚡' : '不稳定'}`;
  algoDescriptionEl.textContent = algo.description;
  algoCodeEl.textContent = algo.code;
}

// ======================== 排序执行 ========================
async function startSort() {
  if (isRunning) return;

  const algoKey = algorithmSelect.value;
  const algo = ALGORITHMS[algoKey];
  if (!algo) return;

  isRunning = true;
  isStopped = false;
  stats.comparisons = 0;
  stats.swaps = 0;
  updateStats(0, 0);
  setStatus('运行中…', 'status-running');
  sortBtn.disabled = true;
  stopBtn.disabled = false;
  generateBtn.disabled = true;
  algorithmSelect.disabled = true;
  sizeSlider.disabled = true;
  speedSlider.disabled = true;

  const startTime = performance.now();
  const delay = getDelay(parseInt(speedSlider.value));
  let sortedIndices = new Set();

  try {
    const generator = algo.generator(array);
    let result = generator.next();

    while (!result.done) {
      if (isStopped) {
        setStatus('已停止', 'status-idle');
        break;
      }

      const step = result.value;
      if (!step) {
        result = generator.next();
        continue;
      }

      // 更新统计
      if (step.type === 'compare') stats.comparisons++;
      if (step.type === 'swap') stats.swaps++;
      updateStats(stats.comparisons, stats.swaps);

      // 同步当前数组
      if (step.array) {
        array = step.array;
      }

      // 构建高亮信息
      const highlights = {};
      if (step.type === 'compare') {
        highlights.comparing = step.indices;
      } else if (step.type === 'swap') {
        highlights.swapping = step.indices;
      } else if (step.type === 'sorted') {
        step.indices.forEach(i => sortedIndices.add(i));
        highlights.sorted = Array.from(sortedIndices);
      } else if (step.type === 'pivot') {
        highlights.pivot = step.indices;
      } else if (step.type === 'overwrite') {
        highlights.sorted = Array.from(sortedIndices);
      }

      renderBars(array, highlights);

      if (delay > 0) {
        await sleep(delay);
      }

      result = generator.next();
    }

    // 排序完成
    if (!isStopped) {
      // 最终结果：全部标记为 sorted
      const finalHighlights = { sorted: array.map((_, i) => i) };
      renderBars(array, finalHighlights);
      const elapsed = Math.round(performance.now() - startTime);
      timeEl.textContent = `${elapsed} ms`;
      setStatus('✅ 排序完成!', 'status-done');
    }
  } catch (err) {
    console.error('排序出错:', err);
    setStatus('❌ 出错', 'status-error');
  }

  isRunning = false;
  sortBtn.disabled = false;
  stopBtn.disabled = true;
  generateBtn.disabled = false;
  algorithmSelect.disabled = false;
  sizeSlider.disabled = false;
  speedSlider.disabled = false;
}

// ======================== 工具函数 ========================
function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

// ======================== 重置 ========================
function resetArray() {
  if (isRunning) return;
  const size = parseInt(sizeSlider.value);
  generateArray(size);
  renderBars(array, {});
  stats.comparisons = 0;
  stats.swaps = 0;
  updateStats(0, 0);
  timeEl.textContent = '0 ms';
  setStatus('就绪', 'status-idle');
}

function stopSort() {
  isStopped = true;
}

// ======================== 事件绑定 ========================
generateBtn.addEventListener('click', resetArray);

sortBtn.addEventListener('click', startSort);

stopBtn.addEventListener('click', stopSort);

algorithmSelect.addEventListener('change', () => {
  updateAlgorithmInfo(algorithmSelect.value);
  if (!isRunning) resetArray();
});

sizeSlider.addEventListener('input', () => {
  sizeValue.textContent = sizeSlider.value;
  if (!isRunning) resetArray();
});

speedSlider.addEventListener('input', () => {
  speedValue.textContent = getSpeedLabel(parseInt(speedSlider.value));
});

// 键盘快捷键支持
document.addEventListener('keydown', (e) => {
  if (e.key === 'r' || e.key === 'R') {
    if (!isRunning) resetArray();
  }
  if (e.key === ' ' || e.key === 'Enter') {
    e.preventDefault();
    if (!isRunning) startSort();
  }
  if (e.key === 's' || e.key === 'S') {
    if (isRunning) stopSort();
  }
});

// ======================== 初始化 ========================
function init() {
  generateArray(parseInt(sizeSlider.value));
  renderBars(array);
  updateAlgorithmInfo(algorithmSelect.value);
  setStatus('就绪', 'status-idle');
  speedValue.textContent = getSpeedLabel(parseInt(speedSlider.value));
}

init();
